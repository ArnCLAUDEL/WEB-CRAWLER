package server.state;

import java.io.IOException;
import java.util.logging.Level;

import io.SerializerBuffer;
import protocol.Abort;
import protocol.ClientIdentifier;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Message;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.StartService;
import protocol.StopService;
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
	
	protected boolean send(ClientIdentifier clientId, Message message) {
		serializerBuffer.clear();
		serializerBuffer.put(message.getFlag());
		message.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send(clientId)) {
			Cheat.LOGGER.log(Level.FINER, message + " sent.");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean handleInit(ClientIdentifier clientId, Init init) {
		Cheat.LOGGER.log(Level.FINEST, "Client Init ignored.");
		return false;
	}
	
	@Override
	public void handleForget(ClientIdentifier clientId, Forget forget) {
		Cheat.LOGGER.log(Level.FINEST, "Client Forget ignored.");
	}
	
	@Override
	public void handleStartService(ClientIdentifier clientId, StartService startService) {
		Cheat.LOGGER.log(Level.FINEST, "Client Start Service ignored.");
	}
	
	@Override
	public void handleStopService(ClientIdentifier clientId, StopService stopService) {
		Cheat.LOGGER.log(Level.FINEST, "Client Stop Service ignored.");
	}
	
	@Override
	public void handleReply(ClientIdentifier clientId, Reply reply) {
		Cheat.LOGGER.log(Level.FINEST, "Client Reply ignored.");
	}

	@Override
	public void handleDecline(ClientIdentifier clientId, Decline decline) {
		Cheat.LOGGER.log(Level.FINEST, "Client Decline ignored.");
	}

	@Override
	public void sendOk(ClientIdentifier clientId, Ok ok) {
		Cheat.LOGGER.log(Level.FINEST, "Server Ok sending ignored.");
		
	}

	@Override
	public void sendAbort(ClientIdentifier clientId, Abort abort) {
		Cheat.LOGGER.log(Level.FINEST, "Server Abort sending ignored.");
		
	}

	@Override
	public void sendRequest(ClientIdentifier clientId, Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Server Request sending ignored.");
	}
	
}
