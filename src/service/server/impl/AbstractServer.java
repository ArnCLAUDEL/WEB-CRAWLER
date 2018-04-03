package service.server.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.logging.Level;

import io.AbstractIOEntity;
import service.message.Abort;
import service.message.Decline;
import service.message.Forget;
import service.message.Init;
import service.message.Ok;
import service.message.Reply;
import service.message.Request;
import service.message.StartService;
import service.message.StopService;
import service.server.ClientIdentifier;
import service.server.Explorer;
import service.server.Server;
import service.server.ServerNetworkHandler;
import service.server.ServerProtocolHandler;
import service.server.impl.state.InactiveServerProtocolHandler;
import util.Cheat;
import util.SerializerBuffer;

public abstract class AbstractServer extends AbstractIOEntity implements Server {
	
	protected final Set<ClientIdentifier> clients;
	protected final Set<ClientIdentifier> activeClients;
	protected final Deque<ClientIdentifier> clientQueue = new ArrayDeque<>();	
	protected final InetSocketAddress address;
	
	protected Explorer explorer;
	protected ServerProtocolHandler protocolHandler;
	protected ServerNetworkHandler networkHandler;
	protected boolean active;
	
	public AbstractServer(int port) {
		super("Server " + Cheat.getId());
		this.address = new InetSocketAddress("localhost", port);
		
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
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	protected void start() throws IOException {}
	
	@Override
	public boolean handleInit(ClientIdentifier clientId, Init init) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + init + "..");
		boolean res = protocolHandler.handleInit(clientId, init);
		if(res)
			Cheat.LOGGER.log(Level.INFO, clientId + " registered.");
		else
			Cheat.LOGGER.log(Level.FINE, clientId + " init ignored.");
		return res;
	}

	@Override
	public void handleStartService(ClientIdentifier clientId, StartService startService) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + startService + "..");
		protocolHandler.handleStartService(clientId, startService);
	}

	@Override
	public void handleStopService(ClientIdentifier clientId, StopService stopService) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + stopService + "..");
		protocolHandler.handleStopService(clientId, stopService);
	}

	@Override
	public void handleReply(ClientIdentifier clientId, Reply reply) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + reply + "..");
		protocolHandler.handleReply(clientId, reply);
	}
	
	@Override
	public void processReply(Reply reply) {
		Cheat.LOGGER.log(Level.FINER, "Processing " + reply + "..");
		explorer.processReply(reply);
	}

	@Override
	public void handleForget(ClientIdentifier clientId, Forget forget) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + forget + "..");
		protocolHandler.handleForget(clientId, forget);
	}

	@Override
	public void handleDecline(ClientIdentifier clientId, Decline decline) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + decline + "..");
		protocolHandler.handleDecline(clientId, decline);
	}

	@Override
	public void sendOk(ClientIdentifier clientId, Ok ok) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + ok + "..");
		protocolHandler.sendOk(clientId, ok);
	}

	public void sendAbort(ClientIdentifier clientId, Abort abort) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + abort + "..");
		protocolHandler.sendAbort(clientId, abort);
	}

	@Override
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
	
	@Override
	public int write(ClientIdentifier clientId, SerializerBuffer serializerBuffer) throws IOException {
		return networkHandler.write(clientId, serializerBuffer);
	}
	
	@Override
	public void update(ClientIdentifier oldClientId, ClientIdentifier newClientId) {
		networkHandler.update(oldClientId, newClientId);
	}
	
	@Override
	public void scan(String hostname) {
		Cheat.LOGGER.log(Level.INFO, "Preparing request.");
		if(hostname.contains("\n"))
			hostname=hostname.replaceAll("\n", "");
		explorer = new Explorer(this, hostname);
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
}
