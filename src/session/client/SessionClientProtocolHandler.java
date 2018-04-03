package session.client;

import java.net.SocketAddress;
import java.util.concurrent.Future;

import session.SessionInfo;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionReply;
import session.message.SessionRequest;

public interface SessionClientProtocolHandler {
	Future<SessionInfo> sendSessionRequest(SocketAddress to, SessionRequest request);
	void handleSessionReply(SocketAddress from, SessionReply reply);
	void sendSessionInit(SocketAddress to, SessionInit init);
	void handleSessionAck(SocketAddress from, SessionAck ack);
}
