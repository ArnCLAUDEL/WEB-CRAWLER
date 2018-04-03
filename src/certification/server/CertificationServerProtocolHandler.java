package certification.server;

import java.net.SocketAddress;

import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;

public interface CertificationServerProtocolHandler {
	void handleCertificationRequest(SocketAddress from, CertificationRequest request);
	void handleCertificationGet(SocketAddress from, CertificationGet get);
	void sendCertificationReply(SocketAddress to, CertificationReply reply);
}
