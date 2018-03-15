package server.state;

import java.util.logging.Level;

import protocol.Abort;
import protocol.ClientIdentifier;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.StartService;
import protocol.StopService;
import server.Server;
import util.Cheat;

public class ActiveServerProtocolHandler extends AbstractServerProtocolHandler {

	public ActiveServerProtocolHandler(Server server) {
		super(server);
	}
	
	@Override
	public boolean handleInit(ClientIdentifier clientId, Init init) {
		boolean res = server.addClient(clientId);
		if(res)
			Cheat.LOGGER.log(Level.FINER, "Client identified.");
		else
			Cheat.LOGGER.log(Level.FINEST, "New client identifier ignored.");
		sendOk(clientId, new Ok(clientId.getId()));
		return res;
	}
	
	@Override
	public void sendOk(ClientIdentifier clientId, Ok ok) {
		// TODO
		send(clientId, ok);
	}
	
	@Override
	public void sendAbort(ClientIdentifier clientId, Abort abort) {
		// TODO
		send(clientId, abort);
	}
	
	@Override
	public void sendRequest(ClientIdentifier clientId, Request request) {
		// TODO 
		send(clientId, request);
	}

	@Override
	public void handleForget(ClientIdentifier clientId, Forget forget) {
		server.removeClient(clientId);
		Cheat.LOGGER.log(Level.FINER, "Client removed.");
	}

	@Override
	public void handleStartService(ClientIdentifier clientId, StartService startService) {
		server.setClientActivity(clientId, true);
		sendOk(clientId, new Ok(clientId.getId()));
		Cheat.LOGGER.log(Level.FINER, "Client activity started.");
	}
	
	@Override
	public void handleStopService(ClientIdentifier clientId, StopService stopService) {
		server.setClientActivity(clientId, false);
		Cheat.LOGGER.log(Level.FINER, "Client activity stopped.");
	}

	@Override
	public void handleReply(ClientIdentifier clientId, Reply reply) {
		// TODO
		reply.getUrls().stream().forEach(System.out::println);
		Cheat.LOGGER.log(Level.INFO, "Client Reply handled.");
	}
	
	@Override
	public void handleDecline(ClientIdentifier clientId, Decline decline) {
		// TODO 
		Cheat.LOGGER.log(Level.INFO, "Client Decline handled. (Not yet implemented)");
	}

}
