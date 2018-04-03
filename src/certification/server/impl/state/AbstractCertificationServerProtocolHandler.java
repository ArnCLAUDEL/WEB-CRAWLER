package certification.server.impl.state;

import java.net.SocketAddress;
import java.util.logging.Level;

import certification.message.CertificationGet;
import certification.message.CertificationReply;
import certification.message.CertificationRequest;
import certification.server.CertificationServerProtocolHandler;
import protocol.AbstractProtocolHandler;
import protocol.NetworkWriter;
import util.Cheat;

public abstract class AbstractCertificationServerProtocolHandler extends AbstractProtocolHandler implements CertificationServerProtocolHandler{
	
	protected AbstractCertificationServerProtocolHandler() {
		super();
	}
	
	protected AbstractCertificationServerProtocolHandler(NetworkWriter networkWriter) {
		super(networkWriter);
	}
	
	@Override
	public void handleCertificationRequest(SocketAddress from, CertificationRequest request) {
		Cheat.LOGGER.log(Level.FINEST, request + " ignored.");
	}
	
	@Override
	public void handleCertificationGet(SocketAddress from, CertificationGet get) {
		Cheat.LOGGER.log(Level.FINEST, get + " ignored.");
	}

	@Override
	public void sendCertificationReply(SocketAddress to, CertificationReply reply) {
		Cheat.LOGGER.log(Level.FINEST, reply + " ignored.");
	}
	
}
