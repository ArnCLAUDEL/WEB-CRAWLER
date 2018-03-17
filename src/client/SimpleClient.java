package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import javax.xml.crypto.dsig.CanonicalizationMethod;

import client.state.InitClientProtocolHandler;
import client.state.NotConnectedClientProtocolHandler;
import io.AbstractIOEntity;
import process.ProcessExecutor;
import protocol.Abort;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.StartService;
import protocol.StopService;
import util.Cheat;

public class SimpleClient extends AbstractIOEntity implements Client {

	private final String hostname;
	private final int port;
	private final ProcessExecutor executor;
	
	private long id;
	private SocketChannel channel;
	private boolean connected;
	private ClientProtocolHandler protocolHandler;
	
	public SimpleClient(String hostname, int port, long previousId) {
		super("Client " + Cheat.getId());
		this.id = previousId;
		this.hostname = hostname;
		this.port = port;
		this.executor = new ProcessExecutor();
		this.connected = false;
		this.protocolHandler = new NotConnectedClientProtocolHandler(this);
	}
	
	public SimpleClient(String hostname, int port) {
		this(hostname, port, Cheat.DEFAULT_ID);
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	@Override
	public void scan(String hostname) {
		Future<Set<String>> future = executor.scan(hostname);
		CompletableFuture.supplyAsync(() -> {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
			}
			return null;
		}).thenAccept(urls -> {
			Reply reply = new Reply(hostname, urls);
			sendReply(reply);
		}).whenComplete((v,e) -> {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		});
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
		protocolHandler.sendInit(new Init(getName(), ProcessExecutor.TASK_CAPACITY, ProcessExecutor.THREAD_CAPACITY, id));
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

	public void sendInit(Init init) {
		protocolHandler.sendInit(init);
	}

	public void sendStartService(StartService startService) {
		protocolHandler.sendStartService(startService);
	}

	public void sendStopService(StopService stopService) {
		protocolHandler.sendStopService(stopService);
	}

	public void sendReply(Reply reply) {
		protocolHandler.sendReply(reply);
	}

	public void sendDecline(Decline decline) {
		protocolHandler.sendDecline(decline);
	}

	public void sendForget(Forget forget) {
		protocolHandler.sendForget(forget);
	}

	public void handleRequest(Request request) {
		protocolHandler.handleRequest(request);
	}

	public void handleOk(Ok ok) {
		protocolHandler.handleOk(ok);
	}

	public void handleAbort(Abort abort) {
		protocolHandler.handleAbort(abort);
	}

	@Override
	public void setProtocolHandler(ClientProtocolHandler protocolHandler) {
		Cheat.LOGGER.log(Level.INFO, this + " switching from state " + this.protocolHandler + " to " + protocolHandler);
		this.protocolHandler = protocolHandler;
	}

	public static void main(String[] args) throws IOException {
		Cheat.setLoggerLevelDisplay(Level.FINER);
		
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
