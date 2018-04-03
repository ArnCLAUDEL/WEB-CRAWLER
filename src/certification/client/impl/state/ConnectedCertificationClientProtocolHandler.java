package certification.client.impl.state;

import java.net.SocketAddress;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import certification.Certificate;
import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;
import protocol.NetworkWriter;

public class ConnectedCertificationClientProtocolHandler extends AbstractCertificationClientProtocolHandler {

	private final Map<Long, CompletableFuture<Certificate>> results;
	
	public ConnectedCertificationClientProtocolHandler(NetworkWriter networWiter) {
		super(networWiter);
		this.results = new TreeMap<>();
	}
	
	@Override
	public Future<Certificate> sendCertertificationRequest(SocketAddress to, CertificationRequest request) {
		CompletableFuture<Certificate> result = new CompletableFuture<>();
		results.put(request.getId(), result);
		send(to, request);
		return result;
	}

	@Override
	public Future<Certificate> sendCertificationGet(SocketAddress to, CertificationGet request) {
		CompletableFuture<Certificate> result = new CompletableFuture<>();
		results.put(request.getId(), result);
		send(to, request);
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
	}

}
