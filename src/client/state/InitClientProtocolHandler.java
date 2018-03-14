package client.state;

import java.nio.channels.SocketChannel;

import client.Client;
import protocol.Decline;
import protocol.Init;
import protocol.Request;

public class InitClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public InitClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}

	@Override
	public void sendInit(Init init) {
		if(send(init))
			client.setProtocolHandler(new InitSentClientProtocolHandler(client, channel));
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
		return "Init";
	}
	
}
