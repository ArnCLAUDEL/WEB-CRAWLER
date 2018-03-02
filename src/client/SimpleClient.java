package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import util.Cheat;

public class SimpleClient implements Client {

	private final Set<AbstractHandler> handlers = new HashSet<>();
	private final String hostname;
	private final int port;
	
	private boolean connected;
	private boolean stop;
	
	public SimpleClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		this.connected = false;
		this.stop = false;
	}
	
	@Override
	public void shutdown() {
		stop = true;
		handlers.stream().forEach(AbstractHandler::shutdown);
		synchronized (this) {
			notifyAll();
		}
	}
	
	@Override
	public void run() {
		Cheat.LOGGER.log(Level.INFO, this + " starting.");
		try {
			SocketChannel channel = connect(hostname, port);
			Cheat.LOGGER.log(Level.INFO, this + " connected.");
			addHandler(new SimpleNetworkHandler(channel, this));
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
	public SocketChannel connect(String hostname, int port) throws IOException {
		SocketChannel sc = SocketChannel.open(new InetSocketAddress(hostname, port));
		this.connected = true;
		return sc;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void addHandler(AbstractHandler handler) {
		this.handlers.add(handler);
		new Thread(handler).start();
	}

	@Override
	public void removeHandler(AbstractHandler handler) {
		this.handlers.remove(handler);
		handler.shutdown();
	}
	
	@Override
	public String toString() {
		return "Client " + Thread.currentThread().getId();
	}

}
