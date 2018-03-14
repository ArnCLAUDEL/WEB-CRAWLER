package client.state;

import java.nio.channels.SocketChannel;

import client.Client;
import protocol.Init;

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
	public String toString() {
		return "Init";
	}
	
}
