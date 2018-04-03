package certification.server.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import certification.Certificate;
import certification.CertificateRequest;
import certification.server.CertificationProvider;

public class SimpleCertificationProvider implements CertificationProvider {
	private final Random RANDOM = new Random();
	
	private final Set<Long> knownId = new HashSet<>();
	
	@Override
	public Certificate make(CertificateRequest request) {
		Date date = Date.from(Instant.now());
		long id;
		do {
			id = generateId();
		} while (knownId.contains(id));
		
		return new Certificate(id, request.getName(), request.getType(), date, request.getHostname(), request.getPort());
	}
	
	private long generateId() {
		return RANDOM.nextLong();
	}

}
