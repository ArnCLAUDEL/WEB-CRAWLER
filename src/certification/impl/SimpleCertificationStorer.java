package certification.impl;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import certification.Certificate;
import certification.CertificateIdentifier;
import certification.CertificationStorer;

public class SimpleCertificationStorer implements CertificationStorer {

	private final Map<CertificateIdentifier, Certificate> certificates = new TreeMap<>();
	
	@Override
	public boolean store(Certificate certificate) {
		CertificateIdentifier identifier = certificate.getIdentifier();
		synchronized (certificates) {
			if(certificates.containsKey(identifier))
				return false;
			certificates.put(identifier, certificate);
			return true;
		}		
	}

	@Override
	public Certificate get(CertificateIdentifier identifier) throws NoSuchElementException {
		if(!certificates.containsKey(identifier))
			throw new NoSuchElementException("Certificate " + identifier.getId() + " not found.");
		return certificates.get(identifier);
	}

}
