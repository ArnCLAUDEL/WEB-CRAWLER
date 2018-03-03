package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Level;

import io.AbstractIOEntity;
import util.Cheat;

public class SimpleServer extends AbstractIOEntity implements Server {

	private final int port;
	
	private ServerSocketChannel serverSocket;
	private InetSocketAddress address;
	private boolean active;
	
	public SimpleServer(int port) {
		super();
		this.port = port;
		this.address = new InetSocketAddress(port);
		this.active = false;
	}
	
	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.ALL);
		
		Server server = new SimpleServer(8080);
		Thread t1;
		
		t1 = new Thread(server);
		
		t1.start();
		try { t1.join();} 
		catch (InterruptedException e) {}
		finally {System.exit(0);}
	}
	
	@Override
	protected void init() throws IOException {
		serverSocket = ServerSocketChannel.open();
		serverSocket.bind(address);
		active = true;
	}
	
	@Override
	protected void startHandlers() throws IOException {
		addHandler(new ServerNetworkHandler(serverSocket, this));
	}
	
	@Override
	public boolean isActive() {
		return active;
	}	

	@Override
	public String toString() {
		return "Server " + Thread.currentThread().getId();
	}
	
}
