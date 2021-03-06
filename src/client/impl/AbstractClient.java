package client.impl;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import client.Client;
import client.ClientNetworkHandler;
import client.ClientProtocolHandler;
import client.impl.state.NotConnectedClientProtocolHandler;
import io.AbstractIOEntity;
import process.ProcessExecutor;
import protocol.message.Abort;
import protocol.message.Decline;
import protocol.message.Forget;
import protocol.message.Init;
import protocol.message.Ok;
import protocol.message.Reply;
import protocol.message.Request;
import protocol.message.StartService;
import protocol.message.StopService;
import util.Cheat;
import util.SerializerBuffer;

/**
 * {@code AbstractClient} is a base implementation of a {@link Client}.<br />
 * This implementation provides a {@link ClientProtocolHandler} but <b>not</b>
 * a {@link ClientNetworkHandler}. This has to be manually set. 
 */
public abstract class AbstractClient extends AbstractIOEntity implements Client {
	protected final String hostname;
	protected final ProcessExecutor executor;
	
	protected int port;
	protected long id;
	protected boolean connected;
	protected ClientProtocolHandler protocolHandler;
	protected ClientNetworkHandler networkHandler;
	
	public AbstractClient(String hostname, int port, long previousId) {
		super("Client " + Cheat.getId());
		this.port = port;
		this.id = previousId;
		this.hostname = hostname;
		this.executor = new ProcessExecutor();
		this.connected = false;
		this.protocolHandler = new NotConnectedClientProtocolHandler(this);
	}
	
	public void sendInit(Init init) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + init + "..");
		protocolHandler.sendInit(init);
	}

	public void sendStartService(StartService startService) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + startService + "..");
		protocolHandler.sendStartService(startService);
	}

	public void sendStopService(StopService stopService) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + stopService + "..");
		protocolHandler.sendStopService(stopService);
	}

	public void sendReply(Reply reply) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + reply + "..");
		protocolHandler.sendReply(reply);
	}

	public void sendDecline(Decline decline) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + decline + "..");
		protocolHandler.sendDecline(decline);
	}

	public void sendForget(Forget forget) {
		Cheat.LOGGER.log(Level.FINER, "Sending " + forget + "..");
		protocolHandler.sendForget(forget);
	}

	public void handleRequest(Request request) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + request + "..");
		protocolHandler.handleRequest(request);
	}

	public void handleOk(Ok ok) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + ok + "..");
		protocolHandler.handleOk(ok);
	}

	public void handleAbort(Abort abort) {
		Cheat.LOGGER.log(Level.FINER, "Handling " + abort + "..");
		protocolHandler.handleAbort(abort);
	}

	@Override
	public void setProtocolHandler(ClientProtocolHandler protocolHandler) {
		Cheat.LOGGER.log(Level.INFO, this + " switching from state " + this.protocolHandler + " to " + protocolHandler);
		this.protocolHandler = protocolHandler;
	}

	@Override
	public int getNbProcessUnits() {		
		return ProcessExecutor.TASK_CAPACITY;
	}

	@Override
	public int getNbTaskMax() {
		return ProcessExecutor.THREAD_CAPACITY;
	}
	
	@Override
	public boolean isActive() {
		return connected;
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public void scan(String hostname, String link) {
		Future<Set<String>> future = executor.scan(hostname, link);
		CompletableFuture.supplyAsync(() -> {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
			}
			return null;
		}).thenAccept(urls -> {
			Reply reply = new Reply(hostname, link, urls);
			sendReply(reply);
		}).whenComplete((v,e) -> {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		});
	}
	
	@Override
	public int write(SerializerBuffer serializerBuffer) throws IOException {
		return networkHandler.write(serializerBuffer);
	}
	
	@Override
	public String toString() {
		return "Client "  + getName() + " " + Thread.currentThread().getId();
	}
}
