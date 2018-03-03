package io;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;

public abstract class AbstractHandler implements Runnable {
	private final static int BUFFER_SIZE = 1024;
	
	protected final Channel channel;
	protected final ByteBuffer buffer;
	
	public AbstractHandler(Channel channel) {
		this.channel = channel;
		this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
	}
	
	public abstract void shutdown();
}