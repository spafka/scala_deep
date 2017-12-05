package org.spafka.io.reactor.basic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import javax.xml.bind.DatatypeConverter;

class Handler implements Runnable {
	private static final int MAXIN = 1024;
	private static final int MAXOUT = 1024;
	final SocketChannel socket;
	final SelectionKey sk;
	ByteBuffer input = ByteBuffer.allocate(MAXIN);
	ByteBuffer output = ByteBuffer.allocate(MAXOUT);
	static final int READING = 0, SENDING = 1;
	int state = READING;

	public Handler(Selector sel, SocketChannel c) throws IOException {
		socket = c;
		c.configureBlocking(false);
		// Optionally try first read now
		sk = socket.register(sel, 0);
		sk.attach(this); // 将Handler作为callback对象
		sk.interestOps(SelectionKey.OP_READ); // 第二步,接收Read事件
		sel.wakeup();
	}

	boolean inputIsComplete() {
		return true;
	}

	boolean outputIsComplete() {
		return true;
	}

	void process() {
		byte[] bytes = new byte[input.position()];
		input.flip();
		input.get(bytes);
		input.clear();
		
		System.out.println("input:"+DatatypeConverter.printHexBinary(bytes));
		
		
	}

	public void run() {
		try {
			if (state == READING)
				read();
			else if (state == SENDING)
				send();
		} catch (IOException ex) { }
	}

	void read() throws IOException {
		socket.read(input);
		System.out.println("read");
		if (inputIsComplete()) {
			process();
			state = SENDING;
			// Normally also do first write now
			sk.interestOps(SelectionKey.OP_WRITE); // 第三步,接收write事件
		}
	}

	void send() throws IOException {
		output.clear();
		output.put("111".getBytes());
		output.flip();
		socket.write(output);
		
		System.out.println("write");
		if (outputIsComplete()){
			sk.interestOps(SelectionKey.OP_READ);
			state = READING;
//			sk.cancel(); // write完就结束了, 关闭select key
		}
			

	}
}