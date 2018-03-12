package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import client.state.InitClientProtocolHandler;
import io.AbstractIOEntity;
import protocol.ClientProtocolHandler;
import protocol.Request;
import util.Cheat;

public class SimpleClient extends AbstractIOEntity implements Client {

	private final String hostname;
	private final int port;
	
	private SocketChannel channel;
	private boolean connected;
	private ClientProtocolHandler protocolHandler;
	
	public SimpleClient(String hostname, int port) {
		super();
		this.hostname = hostname;
		this.port = port;
		this.connected = false;
		this.protocolHandler = new InitClientProtocolHandler(this);
	}
	
	@Override
	protected void init() throws IOException {
		this.channel = connect(hostname, port);
	}
	
	@Override
	protected void startHandlers() throws IOException {
		addHandler(new ClientNetworkHandler(channel, this));
		addHandler(new ClientKeyboardHandler(channel));
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

	@Override
	public void handleRequest(Request request) {
		protocolHandler.handleRequest(request);
	}

	@Override
	public void handleOk() {
		protocolHandler.handleOk();
	}

	@Override
	public void setProtocolHandler(ClientProtocolHandler protocolHandler) {
		this.protocolHandler = protocolHandler;
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
}
