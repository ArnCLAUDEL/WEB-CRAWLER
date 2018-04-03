package session.server.impl.state;

import protocol.AbstractProtocolHandler;
import protocol.NetworkWriter;
import session.server.SessionServerProtocolHandler;

public abstract class AbstractSessionServerProtocolHandler extends AbstractProtocolHandler implements SessionServerProtocolHandler {

	protected AbstractSessionServerProtocolHandler() {
		super();
	}
	
	protected AbstractSessionServerProtocolHandler(NetworkWriter writer) {
		super(writer);
	}
	
}