package app.service.server;

import java.net.SocketAddress;
import java.util.logging.Level;

import certification.client.CertificationClientProtocolHandler;
import certification.message.CertificationReply;
import protocol.AbstractMessageHandler;
import protocol.Flag;
import session.explorer.SessionExplorerProtocolHandler;
import session.message.SessionAck;
import session.message.SessionInit;
import util.Cheat;
import util.SerializerBuffer;

public class ExplorerServerUDPMessageHandler extends AbstractMessageHandler {

	private final SocketAddress address;
	private final SessionExplorerProtocolHandler sessionProtocolHandler;
	private final CertificationClientProtocolHandler certificationProtocolHandler;
	
	public ExplorerServerUDPMessageHandler(SerializerBuffer serializerBuffer, SocketAddress address,
			SessionExplorerProtocolHandler sessionProtocolHandler,
			CertificationClientProtocolHandler certificationProtocolHandler) {
		super(serializerBuffer);
		this.address = address;
		this.sessionProtocolHandler = sessionProtocolHandler;
		this.certificationProtocolHandler = certificationProtocolHandler;
	}

	@Override
	protected void handle() {
		synchronized (serializerBuffer) {
			byte flag = serializerBuffer.get();
			switch (flag) {
				case Flag.CERTIFICATION_REPLY: handleCertificationReply(); break;
				case Flag.SESSION_INIT: handleSessionInit(); break;
				case Flag.SESSION_ACK: handleSessionAck(); break;
				default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
			}
		}
	}
	
	private void handleCertificationReply() {
		handleMessage(serializerBuffer, CertificationReply.CREATOR, address, certificationProtocolHandler::handleCertificationReply);
	}
	
	private void handleSessionInit() {
		handleMessage(serializerBuffer, SessionInit.CREATOR, address, sessionProtocolHandler::handleSessionInit);
	}
	
	private void handleSessionAck() {
		handleMessage(serializerBuffer, SessionAck.CREATOR, address, sessionProtocolHandler::handleSessionAck);
	}

}
