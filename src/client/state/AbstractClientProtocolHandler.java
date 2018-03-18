package client.state;

import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import client.Client;
import protocol.Abort;
import protocol.AbstractProtocolHandler;
import protocol.ClientProtocolHandler;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Message;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.StartService;
import protocol.StopService;
import util.Cheat;

public abstract class AbstractClientProtocolHandler extends AbstractProtocolHandler implements ClientProtocolHandler {
	protected final Client client;
	protected final SocketChannel channel;
	
	public AbstractClientProtocolHandler(Client client, SocketChannel channel) {
		super();
		this.client = client;
		this.channel = channel;
		this.serializerBuffer.setOverflowCallback(getFlushCallback(channel));
	}
	
	protected boolean send(Message message) {
		return send(channel, message);
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
