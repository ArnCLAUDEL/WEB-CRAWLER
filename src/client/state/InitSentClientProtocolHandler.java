package client.state;

import client.Client;

public class InitSentClientProtocolHandler extends AbstractClientProtocolHandler {

	public InitSentClientProtocolHandler(Client client) {
		super(client);
	}

	@Override
	public void handleOk() {
		client.setProtocolHandler(new InactiveClientProtocolHandler(client));
	}
	
}
