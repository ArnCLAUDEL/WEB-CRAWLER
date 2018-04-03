package service.client.impl.state;

import service.client.Client;
import service.message.Decline;
import service.message.Forget;
import service.message.Request;
import service.message.StartService;

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
