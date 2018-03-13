package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import client.state.InitClientProtocolHandler;
import client.state.NotConnectedClientProtocolHandler;
import io.AbstractIOEntity;
import process.ProcessExecutor;
import protocol.Reply;
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
		this.protocolHandler = new NotConnectedClientProtocolHandler(this);
	}
	
	@Override
	protected void init() throws IOException {
		this.channel = connect(hostname, port);
		this.protocolHandler = new InitClientProtocolHandler(this, channel);
	}
	
	@Override
	protected void startHandlers() throws IOException {
		addHandler(new ClientNetworkHandler(channel, this));
		addHandler(new ClientKeyboardHandler(channel));
	}
	
	@Override
	protected void start() throws IOException {
		protocolHandler.sendInit();
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
		return "Client "  + getName() + " " + Thread.currentThread().getId();
	}

	public void sendInit() {
		protocolHandler.sendInit();
	}

	public void sendStartService() {
		protocolHandler.sendStartService();
	}

	public void sendStopService() {
		protocolHandler.sendStopService();
	}

	public void sendReply(Reply reply) {
		protocolHandler.sendReply(reply);
	}

	public void sendDecline(Request request) {
		protocolHandler.sendDecline(request);
	}

	public void sendForget() {
		protocolHandler.sendForget();
	}

	public void handleRequest(Request request) {
		protocolHandler.handleRequest(request);
	}

	public void handleOk() {
		protocolHandler.handleOk();
	}

	public void handleAbort(Request request) {
		protocolHandler.handleAbort(request);
	}

	@Override
	public void setProtocolHandler(ClientProtocolHandler protocolHandler) {
		Cheat.LOGGER.log(Level.INFO, this + " switching from state " + this.protocolHandler + " to " + protocolHandler);
		this.protocolHandler = protocolHandler;
	}

	public static void main(String[] args) throws IOException {
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
	public int getNbProcessUnits() {		
		return ProcessExecutor.TASK_CAPACITY;
	}

	@Override
	public int getNbTaskMax() {
		return ProcessExecutor.THREAD_CAPACITY;
	}
	
}
