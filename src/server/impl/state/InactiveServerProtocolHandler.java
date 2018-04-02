package server.impl.state;

import server.Server;

public class InactiveServerProtocolHandler extends AbstractServerProtocolHandler {

	public InactiveServerProtocolHandler(Server server) {
		super(server);
	}
	
	@Override
	public String toString() {
		return "INACTIVE";
	}
}
