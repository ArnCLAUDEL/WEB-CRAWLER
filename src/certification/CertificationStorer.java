package certification;

import java.util.NoSuchElementException;

public interface CertificationStorer {
	boolean store(Certificate certificate);
	Certificate get(CertificateIdentifier identifier) throws NoSuchElementException;
}
