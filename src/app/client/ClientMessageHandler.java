package app.client;

import java.net.SocketAddress;
import java.util.logging.Level;

import certification.client.CertificationClientProtocolHandler;
import certification.message.CertificationReply;
import protocol.AbstractMessageHandler;
import protocol.Flag;
import service.client.ClientProtocolHandler;
import session.client.SessionClientProtocolHandler;
import session.message.SessionAck;
import session.message.SessionReply;
import util.Cheat;
import util.SerializerBuffer;

public class ClientMessageHandler extends AbstractMessageHandler {

	private final SocketAddress address;
	private final SessionClientProtocolHandler sessionProtocolHandler;
	private final CertificationClientProtocolHandler certificationProtocolHandler;
	private final ClientProtocolHandler serviceProtocolHandler;
	
	public ClientMessageHandler(SerializerBuffer serializerBuffer, SocketAddress address, SessionClientProtocolHandler sessionProtocolHandler,
			CertificationClientProtocolHandler certificationProtocolHandler,
			ClientProtocolHandler serviceProtocolHandler) {
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
				case Flag.SESSION_REPLY: handleSessionReply(); break;
				case Flag.SESSION_ACK: handleSessionAck(); break;
				default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
			}
		}
	}
	
	private void handleCertificationReply() {
		handleMessage(serializerBuffer, CertificationReply.CREATOR, address, certificationProtocolHandler::handleCertificationReply);
	}
	
	private void handleSessionReply() {
		handleMessage(serializerBuffer, SessionReply.CREATOR, address, sessionProtocolHandler::handleSessionReply);
	}
	
	private void handleSessionAck() {
		handleMessage(serializerBuffer, SessionAck.CREATOR, address, sessionProtocolHandler::handleSessionAck);
	}

}
