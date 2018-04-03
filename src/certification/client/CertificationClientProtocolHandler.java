package certification.client;

import java.net.SocketAddress;
import java.util.concurrent.Future;

import certification.Certificate;
import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;

public interface CertificationClientProtocolHandler {
	Future<Certificate> sendCertertificationRequest(SocketAddress to, CertificationRequest request);
	Future<Certificate> sendCertificationGet(SocketAddress to, CertificationGet request);
	void handleCertificationReply(SocketAddress from, CertificationReply reply);
}
