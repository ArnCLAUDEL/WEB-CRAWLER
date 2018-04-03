package certification;

import java.util.concurrent.Future;

public interface CertificationRetriever {
	Future<Certificate> getCertificate(CertificateIdentifier identifier);
}
