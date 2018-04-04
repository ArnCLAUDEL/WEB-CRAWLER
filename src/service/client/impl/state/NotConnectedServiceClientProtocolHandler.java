package service.client.impl.state;

import service.client.ServiceClient;

public class NotConnectedServiceClientProtocolHandler extends AbstractServiceClientProtocolHandler {

	public NotConnectedServiceClientProtocolHandler(ServiceClient client) {
		super(client);
	}

	@Override
	public String toString() {
		return "NOT_CONNECTED";
	}
	
}
