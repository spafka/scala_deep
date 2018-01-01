## hadoop rpc server分析


1.1  server 构造函数 主要指定了一个handlerQueen 接收read线程的call，处理call，返回response
```java
 server.java
 
   // fixme 
   protected Server(String bindAddress, int port, 
                   Class<? extends Writable> paramClass, int handlerCount, 
                   Configuration conf) 
     throws IOException {
     this.bindAddress = bindAddress;
     this.conf = conf;
     this.port = port;
     this.paramClass = paramClass;
     this.handlerCount = handlerCount;
     this.socketSendBufferSize = 0;
     this.maxQueueSize = handlerCount * conf.getInt(
                                 IPC_SERVER_HANDLER_QUEUE_SIZE_KEY,
                                 IPC_SERVER_HANDLER_QUEUE_SIZE_DEFAULT);
     this.maxRespSize = conf.getInt(IPC_SERVER_RPC_MAX_RESPONSE_SIZE_KEY,
                                    IPC_SERVER_RPC_MAX_RESPONSE_SIZE_DEFAULT);
     this.readThreads = conf.getInt(
         CommonConfigurationKeys.IPC_SERVER_RPC_READ_THREADS_KEY,
         CommonConfigurationKeys.IPC_SERVER_RPC_READ_THREADS_DEFAULT);
     this.callQueue  = new LinkedBlockingQueue<Call>(maxQueueSize); 
     this.maxIdleTime = 2*conf.getInt("ipc.client.connection.maxidletime", 1000);
     this.maxConnectionsToNuke = conf.getInt("ipc.client.kill.max", 10);
     this.thresholdIdleConnections = conf.getInt("ipc.client.idlethreshold", 4000);
     
     // Start the listener here and let it bind to the port
     
     
     listener = new Listener();
     this.port = listener.getAddress().getPort();    
     this.tcpNoDelay = conf.getBoolean("ipc.server.tcpnodelay", false);
 
     // Create the responder here
     responder = new Responder();
     
   }
 
 
```
构造函数中最重要的就是这个，绑定端口 这里  selector= Selector.open(); 有一个多路选择器
而每个read线程里也有 一个独自的选择器，也就是一个server有1+readThreads 个 selector 处理socketChannel的监听。
1.2  listener = new Listener();

```java

    public Listener() throws IOException {
      address = new InetSocketAddress(bindAddress, port);
      // Create a new server socket and set to non blocking mode
      acceptChannel = ServerSocketChannel.open();
      acceptChannel.configureBlocking(false);

      // Bind the server socket to the local host and port
      bind(acceptChannel.socket(), address, backlogLength);
      port = acceptChannel.socket().getLocalPort(); //Could be an ephemeral port
      // create a selector;
      selector= Selector.open();
      readers = new Reader[readThreads];
      readPool = Executors.newFixedThreadPool(readThreads);
      for (int i = 0; i < readThreads; i++) {
        Selector readSelector = Selector.open();
        Reader reader = new Reader(readSelector);
        readers[i] = reader;
        readPool.execute(reader);
      }

      // Register accepts on the server socket with the selector.
      //
      acceptChannel.register(selector, SelectionKey.OP_ACCEPT);
      this.setName("IPC Server listener on " + port);
      this.setDaemon(true);
    }


```
1.3 1个accepet线程分发给read线程，每个read线程
```java
    private class Reader implements Runnable {
      private volatile boolean adding = false;
      private Selector readSelector = null;

      Reader(Selector readSelector) {
        this.readSelector = readSelector;
      }
      @Override
	public void run() {
        LOG.info("Starting SocketReader");
        synchronized (this) {
          while (running) {
            SelectionKey key = null;
            try {
              readSelector.select();
              //fixme  wait notify?? not needed
              while (adding) {
                this.wait(1000);
              }              

              Iterator<SelectionKey> iter = readSelector.selectedKeys().iterator();
              while (iter.hasNext()) {
                key = iter.next();
                iter.remove();
                if (key.isValid()) {
                  if (key.isReadable()) {
                    doRead(key);
                  }
                }
                key = null;
              }
            } catch (InterruptedException e) {
              if (running) {                      // unexpected -- log it
                LOG.info(getName() + " caught: " +
                         StringUtils.stringifyException(e));
              }
            } catch (IOException ex) {
              LOG.error("Error in Reader", ex);
            }
          }
        }
      }
      }
```


主线程监听的selectKey 交给reader线程处理
```java
void doAccept(SelectionKey key) throws IOException,  OutOfMemoryError {
      Connection c = null;
      ServerSocketChannel server = (ServerSocketChannel) key.channel();
      SocketChannel channel;
      while ((channel = server.accept()) != null) {
        channel.configureBlocking(false);
        channel.socket().setTcpNoDelay(tcpNoDelay);
        // fixme 轮询，判断交给哪个reader线程
        Reader reader = getReader();
        try {
          reader.startAdd();
          SelectionKey readKey = reader.registerChannel(channel);
          c = new Connection(readKey, channel, System.currentTimeMillis());
          // 构造一个collection，主要包含socketChannel，call的Byteful，附加在key上
          synchronized (connectionList) {
            connectionList.add(numConnections, c);
            numConnections++;
          }
          if (LOG.isDebugEnabled())
            LOG.debug("Server connection from " + c.toString() +
                "; # active connections: " + numConnections +
                "; # queued calls: " + callQueue.size());          
        } finally {
          reader.finishAdd(); 
        }

      }
    }
```

每个read线程对于新进来的selectkey (socketChannel) ，处理其中的connection
1.4 
```java
void doRead(SelectionKey key) throws InterruptedException {
      int count = 0;
      //fixme 从key的附加读取信息，执行方法
      Connection c = (Connection)key.attachment();

      if (c == null) {
        return;  
      }
      c.setLastContact(System.currentTimeMillis());
      
      try {
          
          
        count = c.readAndProcess();
      } catch (InterruptedException ieo) {
        LOG.info(getName() + ": readAndProcess caught InterruptedException", ieo);
        throw ieo;
      } catch (Exception e) {
        LOG.info(getName() + ": readAndProcess threw exception " + e + ". Count of bytes read: " + count, e);
        count = -1; //so that the (count < 0) block is executed
      }
      if (count < 0) {
        if (LOG.isDebugEnabled())
          LOG.debug(getName() + ": disconnecting client " + 
                    c + ". Number of active connections: "+
                    numConnections);
        closeConnection(c);
        c = null;
      }
      else {
        c.setLastContact(System.currentTimeMillis());
      }
    }   
```


```java
public int readAndProcess() throws IOException, InterruptedException {
      while (true) {
        /* Read at most one RPC. If the header is not read completely yet
         * then iterate until we read first RPC or until there is no data left.
         */    
        int count = -1;
        if (dataLengthBuffer.remaining() > 0) {
          count = channelRead(channel, dataLengthBuffer);       
          if (count < 0 || dataLengthBuffer.remaining() > 0) 
            return count;
        }
      
        if (!rpcHeaderRead) {
          //Every connection is expected to send the header.
          if (rpcHeaderBuffer == null) {
            rpcHeaderBuffer = ByteBuffer.allocate(1);
          }
          count = channelRead(channel, rpcHeaderBuffer);
          if (count < 0 || rpcHeaderBuffer.remaining() > 0) {
            return count;
          }
          int version = rpcHeaderBuffer.get(0);
          //byte[] method = new byte[] {rpcHeaderBuffer.get(1)};
//          authMethod = AuthMethod.read(new DataInputStream(
//              new ByteArrayInputStream(method)));
          dataLengthBuffer.flip();          
          if (!HEADER.equals(dataLengthBuffer) || version != CURRENT_VERSION) {
            //Warning is ok since this is not supposed to happen.
            LOG.warn("Incorrect header or version mismatch from " + 
                     hostAddress + ":" + remotePort +
                     " got version " + version + 
                     " expected version " + CURRENT_VERSION);
            return -1;
          }
          dataLengthBuffer.clear();
          
          
          
          rpcHeaderBuffer = null;
          rpcHeaderRead = true;
          continue;
        }
        
        if (data == null) {
          dataLengthBuffer.flip();
          dataLength = dataLengthBuffer.getInt();
       
          if (dataLength == Client.PING_CALL_ID) {
            if(!useWrap) { //covers the !useSasl too
              dataLengthBuffer.clear();
              return 0;  //ping message
            }
          }
          if (dataLength < 0) {
            LOG.warn("Unexpected data length " + dataLength + "!! from " + 
                getHostAddress());
          }
          data = ByteBuffer.allocate(dataLength);
        }
        
        count = channelRead(channel, data);
        
        if (data.remaining() == 0) {
          dataLengthBuffer.clear();
          data.flip();
          if (skipInitialSaslHandshake) {
            data = null;
            skipInitialSaslHandshake = false;
            continue;
          }
          boolean isHeaderRead = headerRead;
          // fixme 数据接收完整
          processOneRpc(data.array());
          data = null;
          if (!isHeaderRead) {
            continue;
          }
        } 
        return count;
      }
    }
```

1.5 反射，connection中的buf反射出call，并加上到callQueue中，hander线程会从callQueue中取数据，反射执行
```java

   // fixme 数据流读取
    private void processData(byte[] buf) throws  IOException, InterruptedException {
      DataInputStream dis =
        new DataInputStream(new ByteArrayInputStream(buf));
      int id = dis.readInt();                    // try to read an id
        
      if (LOG.isDebugEnabled())
        LOG.debug(" got #" + id);

      Writable param = ReflectionUtils.newInstance(paramClass, conf);//read param
      ((RPC.Invocation)param).setConf(conf);
      param.readFields(dis);        
       
      Call call = new Call(id, param, this);
      //
      callQueue.put(call);              // queue the call; maybe blocked here
      incRpcCount();  // Increment the rpc count
    }
    
   
```

```java
/** Handles queued calls . */
  private class Handler extends Thread {
    public Handler(int instanceNumber) {
      this.setDaemon(true);
      this.setName("IPC Server handler "+ instanceNumber + " on " + port);
    }

    @Override
    public void run() {
      LOG.info(getName() + ": starting");
      SERVER.set(Server.this);
      ByteArrayOutputStream buf = 
        new ByteArrayOutputStream(INITIAL_RESP_BUF_SIZE);
      while (running) {
        try {
          final Call call = callQueue.take(); // pop the queue; maybe blocked here

          if (LOG.isDebugEnabled())
            LOG.debug(getName() + ": has #" + call.id + " from " +
                      call.connection);
          
          String errorClass = null;
          String error = null;
          Writable value = null;

          CurCall.set(call);
          try {
            // Make the call as the user via Subject.doAs, thus associating
            // the call with the Subject
            // fixme 
        	value = call(call.connection.protocol, call.param, 
                   call.timestamp);
          } catch (Throwable e) {
            LOG.info(getName()+", call "+call+": error: " + e, e);
            errorClass = e.getClass().getName();
            error = StringUtils.stringifyException(e);
          }
          CurCall.set(null);
          synchronized (call.connection.responseQueue) {
            // setupResponse() needs to be sync'ed together with 
            // responder.doResponse() since setupResponse may use
            // SASL to encrypt response data and SASL enforces
            // its own message ordering.
            setupResponse(buf, call, 
                        (error == null) ? Status.SUCCESS : Status.ERROR,
                        value, errorClass, error);
          // Discard the large buf and reset it back to 
          // smaller size to freeup heap
          if (buf.size() > maxRespSize) {
            LOG.warn("Large response size " + buf.size() + " for call " + 
                call.toString());
              buf = new ByteArrayOutputStream(INITIAL_RESP_BUF_SIZE);
            }
            responder.doRespond(call);
          }
        } catch (InterruptedException e) {
          if (running) {                          // unexpected -- log it
            LOG.info(getName() + " caught: " +
                     StringUtils.stringifyException(e));
          }
        } catch (Exception e) {
          LOG.info(getName() + " caught: " +
                   StringUtils.stringifyException(e));
        }
      }
      LOG.info(getName() + ": exiting");
    }

  }
```
handler call 反射，计算结果，并校验版本信息是否匹配
```java
 public Writable call(Class<?> protocol, Writable param, long receivedTime) 
	    throws IOException {
	      try {
	        Invocation call = (Invocation)param;
	        if (verbose) log("Call: " + call);

	        Method method =
	          protocol.getMethod(call.getMethodName(),
	                                   call.getParameterClasses());
	        method.setAccessible(true);

	        long startTime = System.currentTimeMillis();
	        Object value = method.invoke(instance, call.getParameters());
	        int processingTime = (int) (System.currentTimeMillis() - startTime);
	        int qTime = (int) (startTime-receivedTime);
	        if (LOG.isDebugEnabled()) {
	          LOG.debug("Served: " + call.getMethodName() +
	                    " queueTime= " + qTime +
	                    " procesingTime= " + processingTime);
	        }
//	        rpcMetrics.addRpcQueueTime(qTime);
//	        rpcMetrics.addRpcProcessingTime(processingTime);
//	        rpcMetrics.addRpcProcessingTime(call.getMethodName(), processingTime);
	        if (verbose) log("Return: "+value);

	        return new ObjectWritable(method.getReturnType(), value);

	      } catch (InvocationTargetException e) {
	        Throwable target = e.getTargetException();
	        if (target instanceof IOException) {
	          throw (IOException)target;
	        } else {
	          IOException ioe = new IOException(target.toString());
	          ioe.setStackTrace(target.getStackTrace());
	          throw ioe;
	        }
	      } catch (Throwable e) {
	        if (!(e instanceof IOException)) {
	          LOG.error("Unexpected throwable object ", e);
	        }
	        IOException ioe = new IOException(e.toString());
	        ioe.setStackTrace(e.getStackTrace());
	        throw ioe;
	      }
	    }
	  }
```

1.7 hander 处理完之后，交给response线程，过程与read类似，这里。。。。。
```java

    // Processes one response. Returns true if there are no more pending
    // data for this channel.
    //
    private boolean processResponse(LinkedList<Call> responseQueue,
                                    boolean inHandler) throws IOException {
      boolean error = true;
      boolean done = false;       // there is more data for this channel.
      int numElements = 0;
      Call call = null;
      try {
        synchronized (responseQueue) {
          //
          // If there are no items for this channel, then we are done
          //
          numElements = responseQueue.size();
          if (numElements == 0) {
            error = false;
            return true;              // no more data for this channel.
          }
          //
          // Extract the first call
          //
          call = responseQueue.removeFirst();
          SocketChannel channel = call.connection.channel;
          if (LOG.isDebugEnabled()) {
            LOG.debug(getName() + ": responding to #" + call.id + " from " +
                      call.connection);
          }
          //
          // Send as much data as we can in the non-blocking fashion
          //
          int numBytes = channelWrite(channel, call.response);
          if (numBytes < 0) {
            return true;
          }
          if (!call.response.hasRemaining()) {
            call.connection.decRpcCount();
            if (numElements == 1) {    // last call fully processes.
              done = true;             // no more data for this channel.
            } else {
              done = false;            // more calls pending to be sent.
            }
            if (LOG.isDebugEnabled()) {
              LOG.debug(getName() + ": responding to #" + call.id + " from " +
                        call.connection + " Wrote " + numBytes + " bytes.");
            }
          } else {
            //
            // If we were unable to write the entire response out, then 
            // insert in Selector queue. 
            //
            call.connection.responseQueue.addFirst(call);
            
            if (inHandler) {
              // set the serve time when the response has to be sent later
              call.timestamp = System.currentTimeMillis();
              
              incPending();
              try {
                // Wakeup the thread blocked on select, only then can the call 
                // to channel.register() complete.
                writeSelector.wakeup();
                channel.register(writeSelector, SelectionKey.OP_WRITE, call);
              } catch (ClosedChannelException e) {
                //Its ok. channel might be closed else where.
                done = true;
              } finally {
                decPending();
              }
            }
            if (LOG.isDebugEnabled()) {
              LOG.debug(getName() + ": responding to #" + call.id + " from " +
                        call.connection + " Wrote partial " + numBytes + 
                        " bytes.");
            }
          }
          error = false;              // everything went off well
        }
      } finally {
        if (error && call != null) {
          LOG.warn(getName()+", call " + call + ": output error");
          done = true;               // error. no more data for this channel.
          closeConnection(call.connection);
        }
      }
      return done;
    }
```


### 总结 

