package session.explorer;

import java.net.SocketAddress;

import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionStart;
import session.message.SessionStop;

public interface SessionExplorerProtocolHandler {
	void sendSessionStart(SocketAddress to, SessionStart start);	
	void sendSessionStop(SocketAddress to, SessionStop stop);
	void handleSessionInit(SocketAddress from, SessionInit init);
	void sendSessionAck(SocketAddress to, SessionAck ack);
	void handleSessionAck(SocketAddress from, SessionAck ack);
}
