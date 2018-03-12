package server.state;

import java.util.logging.Level;

import protocol.ClientIdentifier;
import protocol.Reply;
import protocol.ServerProtocolHandler;
import server.Server;
import util.Cheat;

public abstract class AbstractServerProtocolHandler implements ServerProtocolHandler {

	protected final Server server;
	
	public AbstractServerProtocolHandler(Server server) {
		this.server = server;
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
	
}
