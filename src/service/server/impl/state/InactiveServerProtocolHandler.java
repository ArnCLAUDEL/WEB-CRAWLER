package service.server.impl.state;

import service.server.Server;

public class InactiveServerProtocolHandler extends AbstractServerProtocolHandler {

	public InactiveServerProtocolHandler(Server server) {
		super(server);
	}
	
	@Override
	public String toString() {
		return "INACTIVE";
	}
}
