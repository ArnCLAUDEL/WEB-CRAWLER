package client.impl.state;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;

import client.Client;
import client.ClientProtocolHandler;
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
import util.Cheat;
import util.SerializerBuffer;

public abstract class AbstractClientProtocolHandler extends AbstractProtocolHandler implements ClientProtocolHandler {
	protected final Client client;
	
	public AbstractClientProtocolHandler(Client client) {
		super();
		this.client = client;
		this.serializerBuffer.setOverflowCallback(getFlushCallback());
	}
	
	protected boolean send(SerializerBuffer serializerBuffer) {
		try {
			int nb = client.write(serializerBuffer);
			Cheat.LOGGER.log(Level.FINEST, nb + " bytes sent.");
			return true;
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Fail to send data.", e);
			return false;
		}
	}
	
	protected Consumer<? super SerializerBuffer> getFlushCallback() {
		return (serializerBuffer) -> {
			Cheat.LOGGER.log(Level.FINER, "Flushing data..");
			serializerBuffer.flip();
			send(serializerBuffer);
			serializerBuffer.clear();
			Cheat.LOGGER.log(Level.FINER, "Data sent and buffer cleared.");
		};
	}
	
	protected synchronized boolean send(Message message) {
		serializerBuffer.clear();
		serializerBuffer.put(message.getFlag());
		message.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send(serializerBuffer)) {
			Cheat.LOGGER.log(Level.FINER, "Message " + message + " sent.");
			return true;
		}
		return false;
	}
	
	@Override
	public void handleRequest(Request request) {
		Cheat.LOGGER.log(Level.FINEST, request + " ignored.");
	}

	@Override
	public void handleOk(Ok ok) {
		Cheat.LOGGER.log(Level.FINEST, ok + " ignored.");
	}
	
	@Override
	public void handleAbort(Abort abort) {
		Cheat.LOGGER.log(Level.FINEST, abort + " ignored.");
	}

	@Override
	public void sendInit(Init init) {
		Cheat.LOGGER.log(Level.FINEST, init + " ignored.");
	}

	@Override
	public void sendStartService(StartService startService) {
		Cheat.LOGGER.log(Level.FINEST, startService + " ignored.");
	}

	@Override
	public void sendStopService(StopService stopService) {
		Cheat.LOGGER.log(Level.FINEST, stopService + " ignored.");
	}

	@Override
	public void sendReply(Reply reply) {
		Cheat.LOGGER.log(Level.FINEST, reply + " ignored.");
	}

	@Override
	public void sendDecline(Decline decline) {
		Cheat.LOGGER.log(Level.FINEST, decline + " ignored.");
	}

	@Override
	public void sendForget(Forget forget) {
		Cheat.LOGGER.log(Level.FINEST, forget + " ignored.");
	}
}
