package certification.client.impl.state;

import java.net.SocketAddress;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import certification.Certificate;
import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;
import protocol.NetworkWriter;

public class ConnectedCertificationClientProtocolHandler extends AbstractCertificationClientProtocolHandler {

	private final Object dataLock = new Object();
	private final Map<Long, CompletableFuture<Certificate>> results;
	private final Map<Long, TimerTask> retries;
	
	public ConnectedCertificationClientProtocolHandler(NetworkWriter networWiter) {
		super(networWiter);
		this.results = new TreeMap<>();
		this.retries = new TreeMap<>();
	}
	
	@Override
	public Future<Certificate> sendCertertificationRequest(SocketAddress to, CertificationRequest request) {
		CompletableFuture<Certificate> result = new CompletableFuture<>();
		TimerTask retry = retry(request, r -> send(to, r));
		
		synchronized (dataLock) {
			retries.put(request.getId(), retry);
			results.put(request.getId(), result);
		}
		schedule(retry, 0, DEFAULT_PERIOD_RETRY);
		return result;
	}

	@Override
	public Future<Certificate> sendCertificationGet(SocketAddress to, CertificationGet request) {
		CompletableFuture<Certificate> result = new CompletableFuture<>();
		TimerTask retry = retry(request, r -> send(to, r));
		synchronized (dataLock) {
			retries.put(request.getId(), retry);
			results.put(request.getId(), result);	
		}
		schedule(retry, 0, DEFAULT_PERIOD_RETRY);
		return result;
	}
	
	@Override
	public void handleCertificationReply(SocketAddress from, CertificationReply reply) {		
		CompletableFuture<Certificate> result = results.get(reply.getId());
		if(reply.getCertificate().isPresent()) {
			result.complete(reply.getCertificate().get());
		} else {
			result.completeExceptionally(new NoSuchElementException(reply.getErrorMessage().get()));
		}
		results.remove(reply.getId());
		if(retries.containsKey(reply.getId())) {
			retries.remove(reply.getId()).cancel();
		}
		
	}

}
