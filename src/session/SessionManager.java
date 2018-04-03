package session;

import java.util.NoSuchElementException;

import certification.Certificate;

public interface SessionManager {
	
	SessionInfo generate(Certificate certificate);
	boolean createSession(SessionInfo info);
	void deleteSession(SessionIdentifier identifier);
	SessionInfo getSession(SessionIdentifier identifier) throws NoSuchElementException;
	SessionInfo getSession() throws NoSuchElementException;
}
