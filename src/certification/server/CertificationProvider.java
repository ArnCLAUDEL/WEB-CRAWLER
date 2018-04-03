package certification.server;

import certification.Certificate;
import certification.CertificateRequest;

public interface CertificationProvider {
	Certificate make(CertificateRequest request);
}
