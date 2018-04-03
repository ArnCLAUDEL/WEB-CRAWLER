package session.explorer.impl.state;

import java.net.SocketAddress;
import java.util.Set;
import java.util.TreeSet;

import certification.CertificationRetriever;
import protocol.NetworkWriter;
import session.SessionManager;
import session.explorer.SessionExplorer;
import session.message.SessionAck;
import session.message.SessionStart;

public class InactiveStartSentSessionExplorerProtocolHandler extends AbstractSessionExplorerProtocolHandler {
	
	private final Set<Long> expectedIds;
	
	public InactiveStartSentSessionExplorerProtocolHandler(NetworkWriter writer, SessionExplorer explorer, CertificationRetriever retriever, SessionManager manager, long expectedId) {
		super(writer, explorer, retriever, manager);
		expectedIds = new TreeSet<>();
		expectedIds.add(expectedId);
	}
	
	@Override
	public void sendSessionStart(SocketAddress to, SessionStart start) {
		send(to, start);
		synchronized (expectedIds) {
			expectedIds.add(start.getId());
		}
	}
	
	@Override
	public void handleSessionAck(SocketAddress from, SessionAck ack) {
		synchronized (expectedIds) {
			if(expectedIds.contains(ack.getId())) {
				explorer.setProtocolHandler(new ActiveSessionExplorerProtocolHandler(networkWriter.get(), explorer, retriever.get(), manager.get()));
			}
		}
	}
	
}
