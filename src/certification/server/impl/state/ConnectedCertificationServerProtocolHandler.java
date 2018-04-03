package certification.server.impl.state;

import java.net.SocketAddress;
import java.util.NoSuchElementException;

import certification.Certificate;
import certification.CertificationStorer;
import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;
import certification.server.CertificationProvider;
import protocol.NetworkWriter;

public class ConnectedCertificationServerProtocolHandler extends AbstractCertificationServerProtocolHandler {

	private final CertificationStorer storer;
	private final CertificationProvider provider;
	
	public ConnectedCertificationServerProtocolHandler(NetworkWriter networkWriter, CertificationStorer storer,
			CertificationProvider provider) {
		super(networkWriter);
		this.storer = storer;
		this.provider = provider;
	}

	@Override
	public void handleCertificationRequest(SocketAddress from, CertificationRequest request) {
		Certificate certificate = provider.make(request.getCertificateRequest());
		CertificationReply reply;
		if(storer.store(certificate)) {
			reply = new CertificationReply(request.getId(), certificate);
		} else {
			reply = new CertificationReply(request.getId(), "This certificate already exists.");
		}
		sendCertificationReply(from, reply);
	}
	
	@Override
	public void handleCertificationGet(SocketAddress from, CertificationGet request) {
		CertificationReply reply;
		try {
			Certificate certificate = storer.get(request.getIdentifier());
			reply = new CertificationReply(request.getId(), certificate);
		} catch (NoSuchElementException e) {
			reply = new CertificationReply(request.getId(), "Certificate not found.");
		}
		sendCertificationReply(from, reply);
	}

	@Override
	public void sendCertificationReply(SocketAddress to, CertificationReply reply) {
		send(to, reply);
	}
	
}
