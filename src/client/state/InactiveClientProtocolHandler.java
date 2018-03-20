package client.state;

import client.Client;
import protocol.Decline;
import protocol.Forget;
import protocol.Request;
import protocol.StartService;

public class InactiveClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public InactiveClientProtocolHandler(Client client) {
		super(client);
	}
	
	@Override
	public void sendStartService(StartService startService) {
		send(startService);
		client.setProtocolHandler(new InactiveStartSentClientProtocolHandler(client, startService));
	}
	
	@Override
	public void sendForget(Forget forget) {
		send(forget);
		client.setProtocolHandler(new InitClientProtocolHandler(client));
	}
	
	@Override
	public void handleRequest(Request request) {
		sendDecline(new Decline(request));
	}
	
	@Override
	public void sendDecline(Decline decline) {
		send(decline);
	}
	
	@Override
	public String toString() {
		return "INACTIVE";
	}
	
}
