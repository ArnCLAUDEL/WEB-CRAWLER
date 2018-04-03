package certification.client.impl.state;

import certification.client.CertificationClientProtocolHandler;
import protocol.AbstractProtocolHandler;
import protocol.NetworkWriter;

public abstract class AbstractCertificationClientProtocolHandler extends AbstractProtocolHandler implements CertificationClientProtocolHandler {

	protected AbstractCertificationClientProtocolHandler() {
		super();
	}
	
	protected AbstractCertificationClientProtocolHandler(NetworkWriter networkWriter) {
		super(networkWriter);
	}
	
}
