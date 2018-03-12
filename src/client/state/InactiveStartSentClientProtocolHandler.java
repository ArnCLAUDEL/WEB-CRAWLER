package client.state;

import client.Client;

public class InactiveStartSentClientProtocolHandler extends AbstractClientProtocolHandler {
	public InactiveStartSentClientProtocolHandler(Client client) {
		super(client);
	}
	
	@Override
	public void handleOk() {
		client.setProtocolHandler(new ActiveClientProtocolHandler(client));
	}
}
