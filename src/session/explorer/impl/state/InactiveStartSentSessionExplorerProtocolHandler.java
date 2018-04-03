package session.explorer.impl.state;

import java.net.SocketAddress;
import java.util.Optional;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeSet;

import certification.CertificationRetriever;
import protocol.NetworkWriter;
import session.SessionManager;
import session.explorer.SessionExplorer;
import session.message.SessionAck;
import session.message.SessionStart;

public class InactiveStartSentSessionExplorerProtocolHandler extends AbstractSessionExplorerProtocolHandler {
	
	private final Set<Long> expectedIds;
	private Optional<TimerTask> retry;
	
	public InactiveStartSentSessionExplorerProtocolHandler(NetworkWriter writer, SessionExplorer explorer, CertificationRetriever retriever, SessionManager manager) {
		super(writer, explorer, retriever, manager);
		expectedIds = new TreeSet<>();
		retry = Optional.empty();
	}
	
	public InactiveStartSentSessionExplorerProtocolHandler(NetworkWriter writer, SessionExplorer explorer, CertificationRetriever retriever, SessionManager manager, SessionStart start, SocketAddress to) {
		this(writer, explorer, retriever, manager);
		expectedIds.add(start.getId());
		retry = Optional.of(retry(start, s -> sendSessionStart(to, s)));
		schedule(retry.get(), DEFAULT_FIRST_TIME_RETRY, DEFAULT_PERIOD_RETRY);
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
				if(retry.isPresent())
					retry.get().cancel();
				explorer.setProtocolHandler(new ActiveSessionExplorerProtocolHandler(networkWriter.get(), explorer, retriever.get(), manager.get()));
			}
		}
	}
	
}
