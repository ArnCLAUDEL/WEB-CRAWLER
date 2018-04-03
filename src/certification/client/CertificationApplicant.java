package certification.client;

import java.util.concurrent.Future;

import certification.Certificate;
import certification.CertificateRequest;

public interface CertificationApplicant {
	Future<Certificate> requestCertification(CertificateRequest request);
}
