package server.state;

import java.util.logging.Level;

import protocol.Abort;
import protocol.AbstractProtocolHandler;
import protocol.ClientIdentifier;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Message;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.StartService;
import protocol.StopService;
import server.Server;
import server.ServerProtocolHandler;
import util.Cheat;

public abstract class AbstractServerProtocolHandler extends AbstractProtocolHandler implements ServerProtocolHandler {

	protected final Server server;
	
	public AbstractServerProtocolHandler(Server server) {
		super();
		this.server = server;
	}
	
	protected boolean send(ClientIdentifier clientId, Message message) {
		return send(clientId.getChannel(), message);
	}
	
	@Override
	public boolean handleInit(ClientIdentifier clientId, Init init) {
		Cheat.LOGGER.log(Level.FINEST, "Client Init ignored.");
		return false;
	}
	
	@Override
	public void handleForget(ClientIdentifier clientId, Forget forget) {
		Cheat.LOGGER.log(Level.FINEST, "Client Forget ignored.");
	}
	
	@Override
	public void handleStartService(ClientIdentifier clientId, StartService startService) {
		Cheat.LOGGER.log(Level.FINEST, "Client Start Service ignored.");
	}
	
	@Override
	public void handleStopService(ClientIdentifier clientId, StopService stopService) {
		Cheat.LOGGER.log(Level.FINEST, "Client Stop Service ignored.");
	}
	
	@Override
	public void handleReply(ClientIdentifier clientId, Reply reply) {
		Cheat.LOGGER.log(Level.FINEST, "Client Reply ignored.");
	}

	@Override
	public void handleDecline(ClientIdentifier clientId, Decline decline) {
		Cheat.LOGGER.log(Level.FINEST, "Client Decline ignored.");
	}

	@Override
	public void sendOk(ClientIdentifier clientId, Ok ok) {
		Cheat.LOGGER.log(Level.FINEST, "Server Ok sending ignored.");
		
	}

	@Override
	public void sendAbort(ClientIdentifier clientId, Abort abort) {
		Cheat.LOGGER.log(Level.FINEST, "Server Abort sending ignored.");
		
	}

	@Override
	public void sendRequest(ClientIdentifier clientId, Request request) {
		Cheat.LOGGER.log(Level.FINEST, "Server Request sending ignored.");
	}
	
}
