package certification.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import certification.Certificate;
import certification.CertificateIdentifier;
import certification.CertificationStorer;

public class SimpleCertificationStorer implements CertificationStorer {

	private final Map<CertificateIdentifier, Certificate> certificates = new HashMap<>();
	
	@Override
	public boolean store(Certificate certificate) {
		CertificateIdentifier identifier = certificate.getIdentifier();
		if(certificates.containsKey(identifier))
			return false;
		certificates.put(identifier, certificate);
		return true;
	}

	@Override
	public Certificate get(CertificateIdentifier identifier) throws NoSuchElementException {
		if(!certificates.containsKey(identifier))
			throw new NoSuchElementException("Certificate not found.");
		return certificates.get(identifier);
	}

}
