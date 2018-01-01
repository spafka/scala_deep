## hadoop rpc 
详见 
! [Hadoop技术内幕：深入解析YARN架构设计与实现原理.pdf]( )

知识点1. jdk 动态代理
![jdk 动态代理](http://blog.csdn.net/shadowsama/article/details/78152341)

```java
public class RpcClient {

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		
		//代理类，动态代理
		DemoService proxy =  RPC.getProxy(DemoService.class, DemoService.versionID,
				new InetSocketAddress(conf.get("client.ip.name"), conf.getInt("name.port", 8888)), conf, 1000);
		System.out.println("client receive:" + proxy.sum(100, 68));
		System.out.println("client receive:" + proxy.sum(888, 666));
		RPC.stopProxy(proxy);
	}

}

```

```java

Rpc.java

  T proxy =
				   (T) Proxy.newProxyInstance(
	            protocol.getClassLoader(), new Class[] { protocol },
	            new Invoker(protocol, addr, conf, factory, rpcTimeout));

	    return proxy;
```
其中最主要的就是Proxy里的 InvocationHandler，会构造远程访问
```java
private static class Invoker implements InvocationHandler {
		private Client.ConnectionId remoteId;
		private Client client;
		private boolean isClosed = false;
		public Invoker(Class<?> protocol,
				InetSocketAddress address, Configuration conf,
				SocketFactory factory, int rpcTimeout) {
			this.remoteId = Client.ConnectionId.getConnectionId(address,
					protocol, rpcTimeout, conf);
			this.client = CLIENTS.getClient(conf, factory);
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
			final boolean logDebug = LOG.isDebugEnabled();
			long startTime = 0;
			if (logDebug) {
			    startTime = System.currentTimeMillis();
			}

			 ObjectWritable value = (ObjectWritable)
			    client.call(new Invocation(method, args), remoteId);
			 if (logDebug) {
			    long callTime = System.currentTimeMillis() - startTime;
			    LOG.debug("Call: " + method.getName() + " " + callTime);
			  }
			  return value.get();
			  }

			    /* close the IPC client that's responsible for this invoker's RPCs */
			    synchronized private void close() {
			      if (!isClosed) {
			        isClosed = true;
			        CLIENTS.stopClient(client);
			      }
			    }

	}
```


代理中构造client对象，与server取得链接，发送call，等待server.response
```java


	/**
	 * Make a call, passing <code>param</code>, to the IPC server defined by
	 * <code>remoteId</code>, returning the value. Throws exceptions if there
	 * are network problems or if the remote code threw an exception.
	 */
	public Writable call(Writable param, ConnectionId remoteId) throws InterruptedException, IOException {
		Call call = new Call(param);
		Connection connection = getConnection(remoteId, call);
		connection.sendParam(call); // send the parameter
		boolean interrupted = false;
		synchronized (call) {
			while (!call.done) {
				try {
					call.wait(); // wait for the result
				} catch (InterruptedException ie) {
					// save the fact that we were interrupted
					interrupted = true;
				}
			}

			if (interrupted) {
				// set the interrupt flag now that we are done waiting
				Thread.currentThread().interrupt();
			}

			if (call.error != null) {
				if (call.error instanceof RemoteException) {
					call.error.fillInStackTrace();
					throw call.error;
				} else { // local exception
					// use the connection because it will reflect an ip change,
					// unlike
					// the remoteId
					throw wrapException(connection.getRemoteAddress(), call.error);
				}
			} else {
				return call.value;
			}
		}
	}

```