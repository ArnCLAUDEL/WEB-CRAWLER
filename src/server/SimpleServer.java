package server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.logging.Level;

import protocol.Abort;
import protocol.ClientIdentifier;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.ServerProtocolHandler;
import protocol.StartService;
import protocol.StopService;
import server.state.ActiveServerProtocolHandler;
import server.state.InactiveServerProtocolHandler;
import util.Cheat;

public class SimpleServer extends AbstractServer {
	
	private final Set<ClientIdentifier> clients;
	private final Set<ClientIdentifier> activeClients;
	private final Deque<ClientIdentifier> clientQueue = new ArrayDeque<>();
	
	private Explorer explorer;
	private ServerSocketChannel serverSocket;
	private ServerProtocolHandler protocolHandler;
	
	private boolean active;
	
	public SimpleServer(int port) {
		super(port);
		this.clients = new TreeSet<>();
		this.activeClients = new TreeSet<>();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				System.out.println(activeClients.size() + " clients registered");
				System.out.println(activeClients.size() + " clients active");
			}
		};
		//new Timer().scheduleAtFixedRate(task, 0, 5_000);
		this.protocolHandler = new InactiveServerProtocolHandler(this);
		this.active = false;
	}
	
	@Override
	public void scan(String hostname) {
		Cheat.LOGGER.log(Level.INFO, "Preparing request.");
		
		explorer = new Explorer(this, Cheat.ONISEP_URL);
		explorer.sendRequests();
		/*
		Request request = new Request("https://en.wiktionary.org/wiki/Captain_Obvious");
		activeClients	.stream()
		.forEach(c -> protocolHandler.sendRequest(c, request));
		
		for(int i = 0; i < 200; i++) {
			Request request = new Request("https://en.wiktionary.org/wiki/"+i);
			activeClients	.stream()
							.forEach(c -> protocolHandler.sendRequest(c, request));
		}
		*/
		
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
		Cheat.LOGGER.log(Level.FINER, "Handling " + init + "..");
		boolean res = protocolHandler.handleInit(clientId, init);
		if(res)
			Cheat.LOGGER.log(Level.INFO, clientId + " registered.");
		else
			Cheat.LOGGER.log(Level.FINE, clientId + " init ignored.");
		return res;
	}

	public void handleStartService(ClientIdentifier clientId, StartService startService) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + startService + "..");
		protocolHandler.handleStartService(clientId, startService);
	}

	public void handleStopService(ClientIdentifier clientId, StopService stopService) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + stopService + "..");
		protocolHandler.handleStopService(clientId, stopService);
	}

	public void handleReply(ClientIdentifier clientId, Reply reply) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + reply + "..");
		protocolHandler.handleReply(clientId, reply);
	}
	
	public void processReply(Reply reply) {
		Cheat.LOGGER.log(Level.FINER, "Processing " + reply + "..");
		explorer.processReply(reply);
	}

	public void handleForget(ClientIdentifier clientId, Forget forget) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + forget + "..");
		protocolHandler.handleForget(clientId, forget);
	}

	public void handleDecline(ClientIdentifier clientId, Decline decline) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + decline + "..");
		protocolHandler.handleDecline(clientId, decline);
	}

	public void sendOk(ClientIdentifier clientId, Ok ok) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + ok + "..");
		protocolHandler.sendOk(clientId, ok);
	}

	public void sendAbort(ClientIdentifier clientId, Abort abort) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + abort + "..");
		protocolHandler.sendAbort(clientId, abort);
	}

	public void sendRequest(ClientIdentifier clientId, Request request) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + request + "..");
		protocolHandler.sendRequest(clientId, request);
	}
	
	public boolean sendRequest(Request request) {
		Cheat.LOGGER.log(Level.FINER, "Selecting a client..");
		/*
		Optional<ClientIdentifier> optionnalClient = activeClients.stream().findAny();
		if(!optionnalClient.isPresent()) {
			Cheat.LOGGER.log(Level.WARNING, "No active client to send request.");
			return false;
		}
		ClientIdentifier clientId = optionnalClient.get();
		*/
		ClientIdentifier clientId = clientQueue.pop();
		sendRequest(clientId, request);
		clientQueue.addLast(clientId);
		return true;
	}

	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.INFO);
		
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
		return clients.add(clientId) | clients.contains(clientId);
	}

	@Override
	public void removeClient(ClientIdentifier clientId) {
		boolean res = clients.remove(clientId) | activeClients.remove(clientId);
		if(res)
			Cheat.LOGGER.log(Level.INFO, clientId + " removed.");
	}

	@Override
	public void setClientActivity(ClientIdentifier clientId, boolean active) {
		if(active) {
			activeClients.add(clientId);
			clientQueue.add(clientId);
			Cheat.LOGGER.log(Level.INFO, clientId + " is now active.");
		} else {
			activeClients.remove(clientId);
			clientQueue.remove(clientId);
			Cheat.LOGGER.log(Level.INFO, clientId + " is now inactive.");
		}
	}
	
}
