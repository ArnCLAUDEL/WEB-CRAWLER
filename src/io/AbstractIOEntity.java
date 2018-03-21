package io;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import util.Cheat;

public abstract class AbstractIOEntity implements IOEntity {
	private final Set<Handler> handlers;
	private final String name;
	
	protected boolean stop;
	
	public AbstractIOEntity(String name) {
		this.handlers = new HashSet<>();
		this.stop = false;
		this.name = name;
	}
	
	public AbstractIOEntity() {
		this("No name");
	}
	
	public String getName() {
		return name;
	}
	
	protected abstract void start() throws IOException;
	
	@Override
	public void run() {
		Cheat.LOGGER.log(Level.INFO, this + " starting.");
		try {
			start();
			while(!stop) {
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {}
			}
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		Cheat.LOGGER.log(Level.INFO, this + " shutting down.");
	}
	
	@Override
	public void shutdown() {
		stop = true;
		handlers.stream().forEach(Handler::shutdown);
		synchronized (this) {
			notifyAll();
		}
	}
	
	@Override
	public void addHandler(Handler handler) {
		this.handlers.add(handler);
		new Thread(handler).start();
	}

	@Override
	public void removeHandler(Handler handler) {
		this.handlers.remove(handler);
		handler.shutdown();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
