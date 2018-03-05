package io;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;

import util.Cheat;

public abstract class AbstractRawInputHandler extends AbstractHandler {

	private final ReadableByteChannel channel;
	
	private boolean stop;
	
	public AbstractRawInputHandler(ReadableByteChannel channel) {
		super();
		this.channel = channel;
		this.stop = false;
	}
	
	@Override
	public void shutdown() {
		stop = true;
	}
	
	@Override
	protected boolean stop() {
		return stop || !channel.isOpen();
	}
	
	@Override
	protected void handle() {
		System.err.println("handling");
		try {
			buffer.clear();
			if(channel.read(buffer) < 0) {
				shutdown();
				return;
			}
			buffer.flip();
			String message = Cheat.CHARSET.decode(buffer).toString();
			System.out.println(message);
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	@Override
	public String toString() {
		return "Raw Input Handler " + Thread.currentThread().getId();
	}

}
