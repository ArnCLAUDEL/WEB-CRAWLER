package server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import protocol.ClientIdentifier;
import protocol.Reply;
import protocol.Request;
import server.state.ActiveServerProtocolHandler;
import server.state.InactiveServerProtocolHandler;
import util.Cheat;

public class SimpleServer extends AbstractServer {
	
	private final Set<ClientIdentifier> clients;
	private final Set<ClientIdentifier> activeClients;

	private ServerSocketChannel serverSocket;
	private ServerProtocolHandler protocolHandler;
	
	private boolean active;
	
	public SimpleServer(int port) {
		super(port);
		this.clients = new TreeSet<>();
		this.activeClients = new TreeSet<>();
		this.protocolHandler = new InactiveServerProtocolHandler(this);
		this.active = false;
	}
	
	@Override
	public void scan(String hostname) {
		Cheat.LOGGER.log(Level.INFO, "Preparing request.");
		Request request = new Request(hostname);
		activeClients	.stream()
						.forEach(c -> protocolHandler.sendRequest(c, request));
		Cheat.LOGGER.log(Level.INFO, "Request sent to clients.");
	}
	
	@Override
	protected void init() throws IOException {
		serverSocket = ServerSocketChannel.open();
		serverSocket.bind(address);
		active = true;
		this.protocolHandler = new ActiveServerProtocolHandler(this);
	}
	
	@Override
	protected void startHandlers() throws IOException {
		addHandler(new ServerNetworkHandler(serverSocket, this));
		addHandler(new ServerKeyboardHandler(this));
	}
	
	@Override
	protected void start() throws IOException {
		
	}
	
	@Override
	public boolean isActive() {
		return active;
	}	

	@Override
	public String toString() {
		return "Server " + Thread.currentThread().getId();
	}

	
	
	public boolean handleInit(ClientIdentifier clientId) {
		return protocolHandler.handleInit(clientId);
	}

	public void handleStartService(ClientIdentifier clientId) {
		protocolHandler.handleStartService(clientId);
	}

	public void handleStopService(ClientIdentifier clientId) {
		protocolHandler.handleStopService(clientId);
	}

	public void handleReply(ClientIdentifier clientId, Reply reply) {
		protocolHandler.handleReply(clientId, reply);
	}

	public void handleForget(ClientIdentifier clientId) {
		protocolHandler.handleForget(clientId);
	}

	public void handleDecline(ClientIdentifier clientId, Request request) {
		protocolHandler.handleDecline(clientId, request);
	}

	public void sendOk(ClientIdentifier clientId) {
		protocolHandler.sendOk(clientId);
	}

	public void sendAbort(ClientIdentifier clientId, Request request) {
		protocolHandler.sendAbort(clientId, request);
	}

	public void sendRequest(ClientIdentifier clientId, Request request) {
		protocolHandler.sendRequest(clientId, request);
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
	public boolean addClient(ClientIdentifier clientId) {
		return clients.add(clientId);
	}

	@Override
	public void removeClient(ClientIdentifier clientId) {
		clients.remove(clientId);
	}

	@Override
	public void setClientActivity(ClientIdentifier clientId, boolean active) {
		if(active)
			activeClients.add(clientId);
		else
			activeClients.remove(clientId);
	}


	
}
