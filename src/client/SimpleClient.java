package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import io.AbstractIOEntity;
import util.Cheat;

public class SimpleClient extends AbstractIOEntity implements Client {

	private final String hostname;
	private final int port;
	
	private SocketChannel channel;
	private boolean connected;
	
	public SimpleClient(String hostname, int port) {
		super();
		this.hostname = hostname;
		this.port = port;
		this.connected = false;
	}
	
	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		
		Client client = new SimpleClient("localhost", 8080);
		Thread t1;
		
		t1 = new Thread(client);
		
		t1.start();
		try { t1.join();} 
		catch (InterruptedException e) {}
		finally {System.exit(0);}
	}
	
	@Override
	public void run() {
		Cheat.LOGGER.log(Level.INFO, this + " starting.");
		try {
			init();
			Cheat.LOGGER.log(Level.INFO, this + " activated.");
			startHandlers();
			ByteBuffer bb = ByteBuffer.allocate(512);
			for(int i = 0; i < 10; i++) {
				bb.clear();
				bb.put(("Message " + i + " from " + this).getBytes());
				bb.flip();
				channel.write(bb);
				try{Thread.sleep(500);}
				catch(InterruptedException e) {}
			}
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
	protected void init() throws IOException {
		this.channel = connect(hostname, port);
	}
	
	@Override
	protected void startHandlers() throws IOException {
		addHandler(new ClientNetworkHandler(channel, this));
	}
	
	@Override
	public boolean isActive() {
		return connected;
	}

	@Override
	public SocketChannel connect(String hostname, int port) throws IOException {
		SocketChannel sc = SocketChannel.open(new InetSocketAddress(hostname, port));
		this.connected = true;
		return sc;
	}

		@Override
	public String toString() {
		return "Client " + Thread.currentThread().getId();
	}

}
