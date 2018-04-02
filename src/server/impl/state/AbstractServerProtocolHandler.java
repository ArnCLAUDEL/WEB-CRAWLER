package server.impl.state;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;

import protocol.AbstractProtocolHandler;
import protocol.Message;
import protocol.message.Abort;
import protocol.message.Decline;
import protocol.message.Forget;
import protocol.message.Init;
import protocol.message.Ok;
import protocol.message.Reply;
import protocol.message.Request;
import protocol.message.StartService;
import protocol.message.StopService;
import server.ClientIdentifier;
import server.Server;
import server.ServerProtocolHandler;
import util.Cheat;
import util.SerializerBuffer;

public abstract class AbstractServerProtocolHandler extends AbstractProtocolHandler implements ServerProtocolHandler {

	protected final Server server;
	
	public AbstractServerProtocolHandler(Server server) {
		super();
		this.server = server;
	}
	
	protected boolean send(ClientIdentifier clientId, SerializerBuffer serializerBuffer) {
		try {
			int nb = server.write(clientId, serializerBuffer);
			Cheat.LOGGER.log(Level.FINEST, nb + " bytes sent.");
			return true;
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Fail to send data.", e);
			return false;
		}
	}
	
	protected Consumer<? super SerializerBuffer> getFlushCallback(ClientIdentifier clientId) {
		return (serializerBuffer) -> {
			Cheat.LOGGER.log(Level.FINER, "Flushing data..");
			serializerBuffer.flip();
			send(clientId, serializerBuffer);
			serializerBuffer.clear();
			Cheat.LOGGER.log(Level.FINER, "Data sent and buffer cleared.");
		};
	}
	
	protected boolean send(ClientIdentifier clientId) {
		return send(clientId, serializerBuffer);
	}
	
	protected synchronized boolean send(ClientIdentifier clientId, Message message) {
		serializerBuffer.clear();
		serializerBuffer.put(message.getFlag());
		message.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send(clientId)) {
			Cheat.LOGGER.log(Level.FINER, "Message " + message + " sent.");
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
