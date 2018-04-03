package certification.server.impl;

import java.net.SocketAddress;
import java.util.logging.Level;

import certification.message.CertificationRequest;
import certification.server.CertificationServerProtocolHandler;
import protocol.AbstractMessageHandler;
import protocol.Flag;
import util.Cheat;
import util.SerializerBuffer;

public class CertificationAuthorityMessageHandler extends AbstractMessageHandler {

	private final CertificationServerProtocolHandler certificationServerProtocolHandler;
	private final SocketAddress from;
	
	public CertificationAuthorityMessageHandler(SerializerBuffer serializerBuffer, CertificationServerProtocolHandler certificationServerProtocolHandler, SocketAddress from) {
		super(serializerBuffer);
		this.certificationServerProtocolHandler = certificationServerProtocolHandler;
		this.from = from;
	}
	
	@Override
	protected void handle() {
		synchronized (serializerBuffer) {
			byte flag = serializerBuffer.get();
			switch(flag) {
				case Flag.CERTIFICATION_REQUEST: handleCertificationRequest(); break;
				default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
			}
		}
	}
	
	private void handleCertificationRequest() {
		handleMessage(serializerBuffer, CertificationRequest.CREATOR, from, certificationServerProtocolHandler::handleCertificationRequest);
	}

}
