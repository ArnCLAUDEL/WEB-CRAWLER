package service.client.impl.state;

import service.client.Client;
import service.message.Decline;
import service.message.Init;
import service.message.Request;

public class InitClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public InitClientProtocolHandler(Client client) {
		super(client);
	}

	@Override
	public void sendInit(Init init) {
		send(init);
		client.setProtocolHandler(new InitSentClientProtocolHandler(client, init));
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
		return "INIT";
	}
	
}
