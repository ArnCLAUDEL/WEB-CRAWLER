package session;

import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

import certification.Certificate;

public interface SessionManager {
	SessionInfo generate(Certificate certificate);
	boolean createSession(SessionInfo info);
	void deleteSession(SessionIdentifier identifier);
	SessionInfo getSession(SessionIdentifier identifier) throws NoSuchElementException;
	SessionInfo getRandomAgent() throws NoSuchElementException;
	SessionInfo getRandomExplorer() throws NoSuchElementException;
	SessionInfo getSession(InetSocketAddress address) throws NoSuchElementException;
}
