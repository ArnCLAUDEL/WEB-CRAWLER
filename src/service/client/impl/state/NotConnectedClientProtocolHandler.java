package service.client.impl.state;

import service.client.Client;

public class NotConnectedClientProtocolHandler extends AbstractClientProtocolHandler {

	public NotConnectedClientProtocolHandler(Client client) {
		super(client);
	}

	@Override
	public String toString() {
		return "NOT_CONNECTED";
	}
	
}
