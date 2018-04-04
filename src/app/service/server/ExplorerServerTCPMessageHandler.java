package app.service.server;

import java.util.logging.Level;

import protocol.AbstractMessageHandler;
import protocol.Flag;
import service.message.ServiceDecline;
import service.message.ServiceReply;
import service.message.ServiceStart;
import service.message.ServiceStop;
import service.server.ServiceServerProtocolHandler;
import session.SessionInfo;
import util.Cheat;
import util.SerializerBuffer;

public class ExplorerServerTCPMessageHandler extends AbstractMessageHandler {
	
	private final SessionInfo info;
	private final ServiceServerProtocolHandler serviceProtocolHandler;
	
	public ExplorerServerTCPMessageHandler(SerializerBuffer serializerBuffer, SessionInfo info,
			ServiceServerProtocolHandler serviceProtocolHandler) {
		super(serializerBuffer);
		this.info = info;
		this.serviceProtocolHandler = serviceProtocolHandler;
	}

	@Override
	protected void handle() {
		synchronized (serializerBuffer) {
			byte flag = serializerBuffer.get();
			switch (flag) {
				case Flag.SERVICE_START: handleServiceStart(); break;
				case Flag.SERVICE_STOP: handleServiceStop(); break;
				case Flag.SERVICE_REPLY: handleServiceReply(); break;
				case Flag.SERVICE_DECLINE: handleServiceDecline(); break;
				default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
			}
		}
	}
	
	private void handleServiceStart() {
		handleMessage(serializerBuffer, ServiceStart.CREATOR, info, serviceProtocolHandler::handleServiceStart);
	}
	
	private void handleServiceStop() {
		handleMessage(serializerBuffer, ServiceStop.CREATOR, info, serviceProtocolHandler::handleServiceStop);
	}
	
	private void handleServiceReply() {
		handleMessage(serializerBuffer, ServiceReply.CREATOR, info, serviceProtocolHandler::handleServiceReply);
	}
	
	private void handleServiceDecline() {
		handleMessage(serializerBuffer, ServiceDecline.CREATOR, info, serviceProtocolHandler::handleServiceDecline);
	}
}
