package server.state;

import java.util.logging.Level;

import protocol.Abort;
import protocol.ClientIdentifier;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
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
		sendOk(clientId);
		return res;
	}
	
	@Override
	public void sendOk(ClientIdentifier clientId) {
		// TODO
		serializerBuffer.clear();
		Ok ok = new Ok();
		ok.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send(clientId))
			Cheat.LOGGER.log(Level.FINER, "Ok sent.");
	}
	
	@Override
	public void sendAbort(ClientIdentifier clientId, Request request) {
		// TODO
		serializerBuffer.clear();
		Abort abort = new Abort();
		abort.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send(clientId))
				Cheat.LOGGER.log(Level.FINER, "Abort sent.");
	}
	
	@Override
	public void sendRequest(ClientIdentifier clientId, Request request) {
		// TODO 
		serializerBuffer.clear();
		request.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send(clientId))
				Cheat.LOGGER.log(Level.FINER, "Request sent.");
	}

	@Override
	public void handleForget(ClientIdentifier clientId) {
		server.removeClient(clientId);
		Cheat.LOGGER.log(Level.FINER, "Client removed.");
	}

	@Override
	public void handleStartService(ClientIdentifier clientId) {
		server.setClientActivity(clientId, true);
		sendOk(clientId);
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
		Cheat.LOGGER.log(Level.INFO, "Client Reply handled. (Not yet implemented)");
	}
	
	@Override
	public void handleDecline(ClientIdentifier clientId, Request request) {
		// TODO 
		Cheat.LOGGER.log(Level.INFO, "Client Decline handled. (Not yet implemented)");
	}

}
