package session.client.impl.state;

import java.net.SocketAddress;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;

import certification.Certificate;
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
	private final Object dataLock = new Object();
	private final Map<Long, CompletableFuture<Certificate>> certificateResults;
	private final Map<Long, CompletableFuture<SessionInfo>> sessionResults;
	private final Map<Long, TimerTask> retries;
	private final Set<Long> expectedIds;
	
	public ConnectedSessionClientProtocolHandler(NetworkWriter writer, SessionClient client,
			CertificationRetriever retriever) {
		super(writer, client, retriever);
		this.sessionResults = new TreeMap<>();
		this.certificateResults = new TreeMap<>();
		this.retries = new TreeMap<>();
		this.expectedIds = new TreeSet<>();
	}
	
	@Override
	public Future<Certificate> sendSessionRequest(SocketAddress to, SessionRequest request) {
		CompletableFuture<Certificate> result = new CompletableFuture<>();
		synchronized (dataLock) {
			expectedIds.add(request.getId());
			certificateResults.put(request.getId(), result);
			TimerTask retry = retry(request, r -> send(to, r));
			retries.put(request.getId(), retry);
			schedule(retry, 0, DEFAULT_PERIOD_RETRY);
		}
		return result;
	}

	@Override
	public void handleSessionReply(SocketAddress from, SessionReply reply) {
		CompletableFuture<Certificate> result;
		long id = reply.getId();
		synchronized (dataLock) {
			if(!expectedIds.contains(id) || !certificateResults.containsKey(id) || !retries.containsKey(id)) {
				Cheat.LOGGER.log(Level.WARNING, "Unknown SessionReply id.");
				return;
			} else {
				retries.remove(id).cancel();
				expectedIds.remove(id);
				result = certificateResults.remove(id);
			}
		}
		if(reply.getCertificate().isPresent()) {
			result.complete(reply.getCertificate().get());
		} else {
			result.completeExceptionally(new NoSuchElementException(reply.getErrorMessage().get()));	
		}
	}

	@Override
	public Future<SessionInfo> sendSessionInit(SocketAddress to, SessionInit init) {
		CompletableFuture<SessionInfo> result = new CompletableFuture<>();
		synchronized (dataLock) {
			expectedIds.add(init.getId());
			sessionResults.put(init.getId(), result);
			TimerTask retry = retry(init, i -> send(to, i));
			retries.put(init.getId(), retry);
			schedule(retry, 0, DEFAULT_PERIOD_RETRY);
		}
		return result;
	}

	@Override
	public void handleSessionAck(SocketAddress from, SessionAck ack) {
		CompletableFuture<SessionInfo> result;
		long id = ack.getId();
		synchronized (dataLock) {
			if(!expectedIds.contains(id) || !sessionResults.containsKey(id)) {
				Cheat.LOGGER.log(Level.WARNING, "Unknown SessionAck id.");
				return;
			} else {
				retries.remove(id).cancel();
				expectedIds.remove(id);
				result = sessionResults.remove(id);
			}
		}
		result.complete(ack.getSessionInfo());
	}
	
}
