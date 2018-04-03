package session.server.impl.state;

import java.net.SocketAddress;

import session.message.SessionAck;
import session.message.SessionReply;
import session.message.SessionRequest;
import session.message.SessionStart;
import session.message.SessionStop;

public class NotConnectedSessionServerProtocolHandler extends AbstractSessionServerProtocolHandler {

	public NotConnectedSessionServerProtocolHandler() {
		super();
	}
	
	@Override
	public void handleSessionRequest(SocketAddress from, SessionRequest request) {}

	@Override
	public void sendSessionReply(SocketAddress to, SessionReply reply) {}

	@Override
	public void handleSessionStart(SocketAddress from, SessionStart start) {}

	@Override
	public void handleSessionStop(SocketAddress from, SessionStop stop) {}

	@Override
	public void sendSessionAck(SocketAddress to, SessionAck ack) {}

}
