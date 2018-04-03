package session.client;

import java.net.SocketAddress;
import java.util.concurrent.Future;

import certification.Certificate;
import session.SessionInfo;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionReply;
import session.message.SessionRequest;

public interface SessionClientProtocolHandler {
	Future<Certificate> sendSessionRequest(SocketAddress to, SessionRequest request);
	void handleSessionReply(SocketAddress from, SessionReply reply);
	Future<SessionInfo> sendSessionInit(SocketAddress to, SessionInit init);
	void handleSessionAck(SocketAddress from, SessionAck ack);
}
