package server.state;

import java.util.logging.Level;

import protocol.ClientIdentifier;
import protocol.Reply;
import server.Server;
import util.Cheat;

public class ActiveServerProtocolHandler extends AbstractServerProtocolHandler {

	public ActiveServerProtocolHandler(Server server) {
		super(server);
	}
	
	@Override
	public boolean handleInit(ClientIdentifier clientId) {
		boolean res = server.addClient(clientId);
		if(res)
			Cheat.LOGGER.log(Level.FINER, "Client identified.");
		else
			Cheat.LOGGER.log(Level.FINEST, "New client identifier ignored.");
		return res;
	}

	@Override
	public void handleForget(ClientIdentifier clientId) {
		server.removeClient(clientId);
		Cheat.LOGGER.log(Level.FINER, "Client removed.");
	}

	@Override
	public void handleStartService(ClientIdentifier clientId) {
		server.setClientActivity(clientId, true);
		Cheat.LOGGER.log(Level.FINER, "Client activity started.");
	}
	
	@Override
	public void handleStopService(ClientIdentifier clientId) {
		server.setClientActivity(clientId, false);
		Cheat.LOGGER.log(Level.FINER, "Client activity stopped.");
	}

	@Override
	public void handleReply(ClientIdentifier clientId, Reply reply) {
		// TODO
		Cheat.LOGGER.log(Level.INFO, "Client reply handled. (Not yet implemented)");
	}

}
