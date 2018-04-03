package session.client.impl.state;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;

import certification.CertificationRetriever;
import protocol.NetworkWriter;
import session.SessionInfo;
import session.client.SessionClient;
import session.message.SessionAck;
import session.message.SessionInit;
import session.message.SessionReply;
import session.message.SessionRequest;
import util.Cheat;

public class ConnectedSessionClientProtocolHandler extends AbstractSessionClientProtocolHandler {

	private final Map<Long, CompletableFuture<SessionInfo>> results;
	private final Set<Long> expectedIds;
	
	protected ConnectedSessionClientProtocolHandler(NetworkWriter writer, SessionClient client,
			CertificationRetriever retriever) {
		super(writer, client, retriever);
		this.results = new TreeMap<>();
		this.expectedIds = new TreeSet<>();
	}
	
	@Override
	public Future<SessionInfo> sendSessionRequest(SocketAddress to, SessionRequest request) {
		CompletableFuture<SessionInfo> result = new CompletableFuture<>();
		results.put(request.getId(), result);
		send(to, request);
		return result;
	}

	@Override
	public void handleSessionReply(SocketAddress from, SessionReply reply) {
		Cheat.LOGGER.log(Level.FINEST, reply + " ignored.");
	}

	@Override
	public void sendSessionInit(SocketAddress to, SessionInit init) {
		expectedIds.add(init.getId());
		send(to, init);
	}

	@Override
	public void handleSessionAck(SocketAddress from, SessionAck ack) {
		if(!expectedIds.contains(ack.getId()) || !results.containsKey(ack.getId()))
			Cheat.LOGGER.log(Level.WARNING, "Unknown SessionAck id.");	
		
		results.get(ack.getId()).complete(ack.getSessionInfo());
		results.remove(ack.getId());
	}
	
}
