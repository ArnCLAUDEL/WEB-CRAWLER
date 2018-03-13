package client.state;

import java.nio.channels.SocketChannel;

import client.Client;

public class NotConnectedClientProtocolHandler extends AbstractClientProtocolHandler {

	public NotConnectedClientProtocolHandler(Client client) {
		super(client, null);
	}

	@Override
	public String toString() {
		return "Not Connected";
	}
	
}
