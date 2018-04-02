package client.impl.state;

import client.Client;
import protocol.message.Decline;
import protocol.message.Init;
import protocol.message.Request;

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
