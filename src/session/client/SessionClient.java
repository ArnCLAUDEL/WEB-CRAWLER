package session.client;

public interface SessionClient extends SessionClientProtocolHandler {
	void setProtocolHandler(SessionClientProtocolHandler handler);
}
