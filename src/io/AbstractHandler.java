package io;

import java.nio.ByteBuffer;
import java.util.logging.Level;

import util.Cheat;

public abstract class AbstractHandler implements Runnable {
	private final static int BUFFER_SIZE = 1024;
	
	protected final ByteBuffer buffer;
	
	public AbstractHandler() {
		this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
	}

	protected abstract boolean stop();
	protected abstract void handle();
	public abstract void shutdown();
	
	@Override
	public void run() {
		Cheat.LOGGER.log(Level.INFO, this + " starting.");
		while(!stop()) {
			handle();
		}
		Cheat.LOGGER.log(Level.INFO, this + " shutting down.");
	}
		
	
}