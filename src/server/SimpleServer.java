package server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import process.ProcessUnit;
import protocol.Abort;
import protocol.ClientIdentifier;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.StartService;
import protocol.StopService;
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
		
		for(int i = 0; i < 200; i++) {
			Request request = new Request("https://en.wiktionary.org/wiki/"+i);
			activeClients	.stream()
							.forEach(c -> protocolHandler.sendRequest(c, request));
		}
		
		
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
	
	public boolean handleInit(ClientIdentifier clientId, Init init) {
		return protocolHandler.handleInit(clientId, init);
	}

	public void handleStartService(ClientIdentifier clientId, StartService startService) {
		protocolHandler.handleStartService(clientId, startService);
	}

	public void handleStopService(ClientIdentifier clientId, StopService stopService) {
		protocolHandler.handleStopService(clientId, stopService);
	}

	public void handleReply(ClientIdentifier clientId, Reply reply) {
		protocolHandler.handleReply(clientId, reply);
	}

	public void handleForget(ClientIdentifier clientId, Forget forget) {
		protocolHandler.handleForget(clientId, forget);
	}

	public void handleDecline(ClientIdentifier clientId, Decline decline) {
		protocolHandler.handleDecline(clientId, decline);
	}

	public void sendOk(ClientIdentifier clientId, Ok ok) {
		protocolHandler.sendOk(clientId, ok);
	}

	public void sendAbort(ClientIdentifier clientId, Abort abort) {
		protocolHandler.sendAbort(clientId, abort);
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
		activeClients.remove(clientId);
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
