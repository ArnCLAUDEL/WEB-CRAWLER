package certification.client.impl.state;

import java.net.SocketAddress;
import java.util.concurrent.Future;

import certification.Certificate;
import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;

public class NotConnectedCertificationClientProtocolHandler extends AbstractCertificationClientProtocolHandler {
	
	public NotConnectedCertificationClientProtocolHandler() {
		super();
	}
	
	@Override
	public Future<Certificate> sendCertertificationRequest(SocketAddress to, CertificationRequest request) {
		return getNotYetConnectedFuture();
	}

	@Override
	public void handleCertificationReply(SocketAddress from, CertificationReply reply) {}

	@Override
	public Future<Certificate> sendCertificationGet(SocketAddress to, CertificationGet request) {
		return getNotYetConnectedFuture();
	}

}
