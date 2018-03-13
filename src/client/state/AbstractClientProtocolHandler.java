package client.state;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import client.Client;
import client.ClientProtocolHandler;
import io.SerializerBuffer;
import protocol.Reply;
import protocol.Request;
import util.Cheat;

public abstract class AbstractClientProtocolHandler implements ClientProtocolHandler {
	protected final Client client;
	protected final SocketChannel channel;
	protected final SerializerBuffer serializerBuffer;
	
	public AbstractClientProtocolHandler(Client client, SocketChannel channel) {
		this.client = client;
		this.channel = channel;
		this.serializerBuffer = new SerializerBuffer();
	}
	
	protected boolean send() {
		try {
			serializerBuffer.write(channel);
			return true;
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Failed to send message from " + client, e);
			return false;
		}
	}
	
	@Override
	public void handleRequest(Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Server Request ignored.");
	}

	@Override
	public void handleOk() {
		Cheat.LOGGER.log(Level.FINEST, "Server Ok ignored.");
	}
	
	@Override
	public void handleAbort(Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Server Abort ignored.");
	}

	@Override
	public void sendInit() {
		Cheat.LOGGER.log(Level.FINEST, "Client Init sending ignored.");
	}

	@Override
	public void sendStartService() {
		Cheat.LOGGER.log(Level.FINEST, "Client Start Service sending ignored.");
	}

	@Override
	public void sendStopService() {
		Cheat.LOGGER.log(Level.FINEST, "Client Stop Service sending ignored.");
	}

	@Override
	public void sendReply(Reply reply) {
		Cheat.LOGGER.log(Level.FINEST, "Client Reply sending ignored.");
	}

	@Override
	public void sendDecline(Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Client Decline sending ignored.");
	}

	@Override
	public void sendForget() {
		Cheat.LOGGER.log(Level.FINEST, "Client Forget sending ignored.");
	}
}
