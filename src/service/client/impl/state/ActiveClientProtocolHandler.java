package service.client.impl.state;

import java.util.logging.Level;

import service.client.Client;
import service.message.Abort;
import service.message.Decline;
import service.message.Forget;
import service.message.Reply;
import service.message.Request;
import service.message.StopService;
import util.Cheat;

public class ActiveClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public ActiveClientProtocolHandler(Client client) {
		super(client);
	}
	
	@Override
	public void sendReply(Reply reply) {
		send(reply);
	}
	
	@Override
	public void sendDecline(Decline decline) {
		send(decline);
	}
	
	@Override
	public void sendStopService(StopService stopService) {
		send(stopService);
		client.setProtocolHandler(new InactiveClientProtocolHandler(client));
	}
	
	@Override
	public void sendForget(Forget forget) {
		send(forget);
		client.setProtocolHandler(new InitClientProtocolHandler(client));
	}
	
	@Override
	public void handleRequest(Request request) {
		client.scan(request.getHostname(),request.getLink());	
	}
	
	@Override
	public void handleAbort(Abort abort) {
		// TODO 
		Cheat.LOGGER.log(Level.INFO, "Server Abort handled. (not yet implemented)");
	}
	
	@Override
	public String toString() {
		return "ACTIVE";
	}
	
}
