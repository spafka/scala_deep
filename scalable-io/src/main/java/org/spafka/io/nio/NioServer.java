package org.spafka.io.nio;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
public class NioServer {
	private static int BUF_SIZE = 1024;
	private int port;
	private Selector selector;
	private ExecutorService threadPool = Executors.newFixedThreadPool(10);

	public NioServer(int port) {
		this.port = port;

	}

	public void start() {
		ServerSocketChannel ssc = null;
		try {
			selector = Selector.open();
			ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(port));
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			log.info("start success,listen on port " + port);

			while (true) {
				/**
				 * select操作执行时其他线程register,将会阻塞.可以在任意时刻关闭通道或者取消键.
				 * 因为select操作并未对Key.cancell()同步,因此有可能再selectedKey中出现的key是已经被取消的.
				 * 这一点需要注意.需要校验:key.isValid() && key.isReadable()
				 */
				if (selector.select(500) == 0) {
					continue;
				}
				Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					if (key.isAcceptable()) {
						log.info("server recieve an accept");
						handleAccept(key);
					}
					if (key.isValid() && key.isReadable()) {
						log.info("server recieve a read");
						handleRead(key);
					}
					if (key.isWritable() && key.isValid()) {
						log.info("server recieve a write");
						handleWrite(key);
					}
					if (key.isConnectable()) {
						log.info("server recieve a isConnectable = true");
					}
					if (!key.isValid()){
						key.cancel();
						log.error("");
					}
					iter.remove();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		selector.wakeup();
		selector.close();
		threadPool.shutdown();
	}

	public static void handleAccept(SelectionKey key) throws IOException {
		ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
		SocketChannel sc = ssChannel.accept();
		sc.configureBlocking(false);
		//
		sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));
		
	}

	public static void handleRead(SelectionKey key) throws IOException {

		SocketChannel sc = (SocketChannel) key.channel();
		ByteBuffer buf = (ByteBuffer) key.attachment();
		long bytesRead = sc.read(buf);
		while (bytesRead > 0) {
			buf.flip();
			while (buf.hasRemaining()) {
				 buf.get();
			}
			buf.clear();
			bytesRead = sc.read(buf);
		}
		if (bytesRead == -1) {
			sc.close();
		}
	}

	public static void handleWrite(SelectionKey key) throws IOException {
		ByteBuffer buf = (ByteBuffer) key.attachment();
		buf.flip();
		SocketChannel sc = (SocketChannel) key.channel();
		while (buf.hasRemaining()) {
			sc.write(buf);
		}
		buf.compact();
	}

	public static void main(String[] args) {
		new NioServer(5555).start();

	}

}
