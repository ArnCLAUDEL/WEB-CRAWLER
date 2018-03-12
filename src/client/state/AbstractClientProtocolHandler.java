package client.state;

import java.util.logging.Level;

import client.Client;
import protocol.ClientProtocolHandler;
import protocol.Request;
import util.Cheat;

public abstract class AbstractClientProtocolHandler implements ClientProtocolHandler {
	protected final Client client;
	
	public AbstractClientProtocolHandler(Client client) {
		this.client = client;
	}
	
	@Override
	public void handleRequest(Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Server Request ignored.");
	}

	@Override
	public void handleOk() {
		Cheat.LOGGER.log(Level.FINEST, "Server Ok ignored.");
	}
}
