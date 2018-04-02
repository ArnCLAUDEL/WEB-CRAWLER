package client.impl.state;

import client.Client;

public class NotConnectedClientProtocolHandler extends AbstractClientProtocolHandler {

	public NotConnectedClientProtocolHandler(Client client) {
		super(client);
	}

	@Override
	public String toString() {
		return "NOT_CONNECTED";
	}
	
}
