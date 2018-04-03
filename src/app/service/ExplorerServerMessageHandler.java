package app.service;

import java.net.SocketAddress;
import java.util.logging.Level;

import certification.client.CertificationClientProtocolHandler;
import certification.message.CertificationReply;
import protocol.AbstractMessageHandler;
import protocol.Flag;
import service.server.ServerProtocolHandler;
import session.explorer.SessionExplorerProtocolHandler;
import session.message.SessionAck;
import session.message.SessionInit;
import util.Cheat;
import util.SerializerBuffer;

public class ExplorerServerMessageHandler extends AbstractMessageHandler {

	private final SocketAddress address;
	private final SessionExplorerProtocolHandler sessionProtocolHandler;
	private final CertificationClientProtocolHandler certificationProtocolHandler;
	private final ServerProtocolHandler serviceProtocolHandler;
	
	public ExplorerServerMessageHandler(SerializerBuffer serializerBuffer, SocketAddress address,
			SessionExplorerProtocolHandler sessionProtocolHandler,
			CertificationClientProtocolHandler certificationProtocolHandler,
			ServerProtocolHandler serviceProtocolHandler) {
		super(serializerBuffer);
		this.address = address;
		this.sessionProtocolHandler = sessionProtocolHandler;
		this.certificationProtocolHandler = certificationProtocolHandler;
		this.serviceProtocolHandler = serviceProtocolHandler;
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
