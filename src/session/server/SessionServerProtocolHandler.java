package session.server;

import java.net.SocketAddress;

import session.message.SessionAck;
import session.message.SessionReply;
import session.message.SessionRequest;
import session.message.SessionStart;
import session.message.SessionStop;

public interface SessionServerProtocolHandler {
	void handleSessionRequest(SocketAddress from, SessionRequest request);
	void sendSessionReply(SocketAddress to, SessionReply reply);
	void handleSessionStart(SocketAddress from, SessionStart start);
	void handleSessionStop(SocketAddress from, SessionStop stop);
	void sendSessionAck(SocketAddress to, SessionAck ack);
}
