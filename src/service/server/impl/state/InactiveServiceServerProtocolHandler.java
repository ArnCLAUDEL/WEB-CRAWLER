package service.server.impl.state;

import service.server.ServiceServer;

public class InactiveServiceServerProtocolHandler extends AbstractServiceServerProtocolHandler {

	public InactiveServiceServerProtocolHandler(ServiceServer server) {
		super(server);
	}
	
	@Override
	public String toString() {
		return "INACTIVE";
	}
}
