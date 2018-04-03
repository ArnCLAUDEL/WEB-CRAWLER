package service.server.impl.state;

import java.util.logging.Level;

import service.message.Abort;
import service.message.Decline;
import service.message.Forget;
import service.message.Init;
import service.message.Ok;
import service.message.Reply;
import service.message.Request;
import service.message.StartService;
import service.message.StopService;
import service.server.ClientIdentifier;
import service.server.Server;
import util.Cheat;

public class ActiveServerProtocolHandler extends AbstractServerProtocolHandler {

	public ActiveServerProtocolHandler(Server server) {
		super(server);
	}
	
	@Override
	public boolean handleInit(ClientIdentifier clientId, Init init) {
		boolean res = server.addClient(clientId);
		if(res)
			sendOk(clientId, new Ok(clientId.getId()));
		return res;
	}
	
	@Override
	public void sendOk(ClientIdentifier clientId, Ok ok) {
		send(clientId, ok);
	}
	
	@Override
	public void sendAbort(ClientIdentifier clientId, Abort abort) {
		send(clientId, abort);
	}
	
	@Override
	public void sendRequest(ClientIdentifier clientId, Request request) { 
		send(clientId, request);
	}

	@Override
	public void handleForget(ClientIdentifier clientId, Forget forget) {
		server.removeClient(clientId);
	}

	@Override
	public void handleStartService(ClientIdentifier clientId, StartService startService) {
		server.setClientActivity(clientId, true);
		sendOk(clientId, new Ok(clientId.getId()));
	}
	
	@Override
	public void handleStopService(ClientIdentifier clientId, StopService stopService) {
		server.setClientActivity(clientId, false);
	}

	@Override
	public void handleReply(ClientIdentifier clientId, Reply reply) {
		server.processReply(reply);
	}
	
	@Override
	public void handleDecline(ClientIdentifier clientId, Decline decline) {
		// TODO 
		Cheat.LOGGER.log(Level.INFO, "Client Decline handled. (Not yet implemented)");
	}

	@Override
	public String toString() {
		return "ACTIVE";
	}
}
