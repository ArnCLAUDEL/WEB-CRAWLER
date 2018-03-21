package client.state;

import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import client.Client;
import protocol.Abort;
import protocol.Decline;
import protocol.Forget;
import protocol.Reply;
import protocol.Request;
import protocol.StopService;
import util.Cheat;

public class ActiveClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public ActiveClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
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
		client.setProtocolHandler(new InactiveClientProtocolHandler(client, channel));
	}
	
	@Override
	public void sendForget(Forget forget) {
		send(forget);
		client.setProtocolHandler(new InitClientProtocolHandler(client, channel));
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
