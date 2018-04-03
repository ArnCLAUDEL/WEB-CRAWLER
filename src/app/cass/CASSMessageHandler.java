package app.cass;

import java.net.SocketAddress;
import java.util.logging.Level;

import certification.message.CertificationGet;
import certification.message.CertificationRequest;
import certification.server.CertificationServerProtocolHandler;
import protocol.AbstractMessageHandler;
import protocol.Flag;
import session.message.SessionRequest;
import session.message.SessionStart;
import session.message.SessionStop;
import session.server.SessionServerProtocolHandler;
import util.Cheat;
import util.SerializerBuffer;

public class CASSMessageHandler extends AbstractMessageHandler {

	private final SocketAddress address;
	private final SessionServerProtocolHandler sessionProtocolHandler;
	private final CertificationServerProtocolHandler certificationProtocolHandler;
	
	public CASSMessageHandler(SerializerBuffer serializerBuffer, SocketAddress address,
			SessionServerProtocolHandler sessionProtocolHandler,
			CertificationServerProtocolHandler certificationProtocolHandler) {
		super(serializerBuffer);
		this.address = address;
		this.sessionProtocolHandler = sessionProtocolHandler;
		this.certificationProtocolHandler = certificationProtocolHandler;
	}

	@Override
	protected void handle() {
		synchronized (serializerBuffer) {
			byte flag = serializerBuffer.get();
			switch(flag) {
				case Flag.CERTIFICATION_REQUEST : handleCertificationRequest(); break;
				case Flag.CERTIFICATION_GET : handleCertificationGet(); break;
				case Flag.SESSION_REQUEST: handleSessionRequest(); break;
				case Flag.SESSION_START: handleSessionStart(); break;
				case Flag.SESSION_STOP: handleSessionStop(); break;
				default : Cheat.LOGGER.log(Level.WARNING, "Unknown protoco flag : " + flag);
			}
		}
	}
	
	private void handleCertificationRequest() {
		handleMessage(serializerBuffer, CertificationRequest.CREATOR, address, certificationProtocolHandler::handleCertificationRequest);
	}
	
	private void handleCertificationGet() {
		handleMessage(serializerBuffer, CertificationGet.CREATOR, address, certificationProtocolHandler::handleCertificationGet);
	}
	
	private void handleSessionRequest() {
		handleMessage(serializerBuffer, SessionRequest.CREATOR, address, sessionProtocolHandler::handleSessionRequest);
	}
	
	private void handleSessionStart() {
		handleMessage(serializerBuffer, SessionStart.CREATOR, address, sessionProtocolHandler::handleSessionStart);
	}
	
	private void handleSessionStop() {
		handleMessage(serializerBuffer, SessionStop.CREATOR, address, sessionProtocolHandler::handleSessionStop);
	}

}
