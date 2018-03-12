package client.state;

import java.util.logging.Level;

import client.Client;
import protocol.Request;
import util.Cheat;

public class ActiveClientProtocolHandler extends AbstractClientProtocolHandler {
	public ActiveClientProtocolHandler(Client client) {
		super(client);
	}
	
	@Override
	public void handleRequest(Request request) {
		// TODO 
		Cheat.LOGGER.log(Level.INFO, "Server request handled. (not yet implemented)");
	}
	
}
