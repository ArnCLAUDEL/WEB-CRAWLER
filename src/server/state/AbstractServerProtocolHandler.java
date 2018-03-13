package server.state;

import java.io.IOException;
import java.util.logging.Level;

import io.SerializerBuffer;
import protocol.ClientIdentifier;
import protocol.Reply;
import protocol.Request;
import server.Server;
import server.ServerProtocolHandler;
import util.Cheat;

public abstract class AbstractServerProtocolHandler implements ServerProtocolHandler {

	protected final Server server;
	protected final SerializerBuffer serializerBuffer;
	
	public AbstractServerProtocolHandler(Server server) {
		this.server = server;
		this.serializerBuffer = new SerializerBuffer();
	}
	
	protected boolean send(ClientIdentifier clientId) {
		try {
			serializerBuffer.write(clientId.getChannel());
			return true;
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Failed to send message from " + server +  " to " + clientId.getName(), e);
			return false;
		}
	}
	
	@Override
	public boolean handleInit(ClientIdentifier clientId) {
		Cheat.LOGGER.log(Level.FINEST, "Client Init ignored.");
		return false;
	}
	
	@Override
	public void handleForget(ClientIdentifier clientId) {
		Cheat.LOGGER.log(Level.FINEST, "Client Forget ignored.");
	}
	
	@Override
	public void handleStartService(ClientIdentifier clientId) {
		Cheat.LOGGER.log(Level.FINEST, "Client Start Service ignored.");
	}
	
	@Override
	public void handleStopService(ClientIdentifier clientId) {
		Cheat.LOGGER.log(Level.FINEST, "Client Stop Service ignored.");
	}
	
	@Override
	public void handleReply(ClientIdentifier clientId, Reply reply) {
		Cheat.LOGGER.log(Level.FINEST, "Client Reply ignored.");
	}

	@Override
	public void handleDecline(ClientIdentifier clientId, Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Client Decline ignored.");
	}

	@Override
	public void sendOk(ClientIdentifier clientId) {
		Cheat.LOGGER.log(Level.FINEST, "Server Ok sending ignored.");
		
	}

	@Override
	public void sendAbort(ClientIdentifier clientId, Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Server Abort sending ignored.");
		
	}

	@Override
	public void sendRequest(ClientIdentifier clientId, Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Server Request sending ignored.");
	}
	
}
