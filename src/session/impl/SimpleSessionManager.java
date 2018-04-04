package session.impl;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import certification.Certificate;
import session.SessionIdentifier;
import session.SessionInfo;
import session.SessionManager;
import util.Cheat;

public class SimpleSessionManager implements SessionManager {

	private final Map<SessionIdentifier, SessionInfo> sessions;
	private final Set<Long> knownIds;
	
	public SimpleSessionManager() {
		sessions = new TreeMap<>();
		knownIds = new TreeSet<>();
	}
	
	@Override
	public synchronized boolean createSession(SessionInfo info) {
		SessionIdentifier identifier = info.getIdentifier();
		synchronized (sessions) {
			if(sessions.containsKey(identifier))
				return false;
			
			sessions.put(identifier, info);
			return true;
		}
	}

	@Override
	public synchronized void deleteSession(SessionIdentifier identifier) {
		synchronized (sessions) {
			sessions.remove(identifier);
		}
	}

	@Override
	public SessionInfo getSession(SessionIdentifier identifier) throws NoSuchElementException {
		synchronized (sessions) {
			if(!sessions.containsKey(identifier))
				throw new NoSuchElementException("Session not found");
			
			return sessions.get(identifier);
		}
	}

	@Override
	public SessionInfo getRandomExplorer() throws NoSuchElementException {
		return getRandom(Certificate.EXPLORER);
	}
	
	@Override
	public SessionInfo getRandomAgent() throws NoSuchElementException {
		return getRandom(Certificate.AGENT);
	}
	
	private SessionInfo getRandom(byte type) throws NoSuchElementException {
		Optional<SessionInfo> info = sessions.values().stream().filter(session -> session.getCertificate().getType() == type).findAny();
		if(!info.isPresent())
			throw new NoSuchElementException("No session found");
		
		return info.get();
	}
	
	@Override
	public SessionInfo generate(Certificate certificate) {
		long id;
		do {
			id = Cheat.RANDOM.nextLong();
		} while(knownIds.contains(id));
		
		return new SessionInfo(id, certificate);
	}
	
	@Override
	public SessionInfo getSession(InetSocketAddress address) throws NoSuchElementException {
		return sessions.values().stream()
						.filter(info -> info.getCertificate().getAddress().getHostName().equals(address.getHostName()))
						.findFirst().get();
	}
}
