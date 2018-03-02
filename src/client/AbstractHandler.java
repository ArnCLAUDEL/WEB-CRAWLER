package client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class AbstractHandler implements Runnable {
	private final static int BUFFER_SIZE = 1024;
	
	protected final SocketChannel channel;
	protected final ByteBuffer buffer;
	
	public AbstractHandler(SocketChannel channel) {
		this.channel = channel;
		this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
	}
	
	public abstract void shutdown();
}