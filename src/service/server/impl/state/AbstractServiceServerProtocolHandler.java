package service.server.impl.state;

import java.util.logging.Level;

import protocol.AbstractProtocolHandler;
import protocol.NetworkWriter;
import service.message.ServiceDecline;
import service.message.ServiceOk;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import service.message.ServiceStart;
import service.message.ServiceStop;
import service.server.ServiceServer;
import service.server.ServiceServerProtocolHandler;
import session.SessionInfo;
import util.Cheat;

public abstract class AbstractServiceServerProtocolHandler extends AbstractProtocolHandler implements ServiceServerProtocolHandler {

	protected final ServiceServer server;
	
	protected AbstractServiceServerProtocolHandler(ServiceServer server) {
		super();
		this.server = server;
	}
	
	protected AbstractServiceServerProtocolHandler(NetworkWriter writer, ServiceServer server) {
		super(writer);
		this.server = server;
	}
	
	@Override
	public void handleServiceStart(SessionInfo info, ServiceStart startService) {
		Cheat.LOGGER.log(Level.FINEST, "Client Start Service ignored.");
	}
	
	@Override
	public void handleServiceStop(SessionInfo info, ServiceStop stopService) {
		Cheat.LOGGER.log(Level.FINEST, "Client Stop Service ignored.");
	}
	
	@Override
	public void handleServiceReply(SessionInfo info, ServiceReply reply) {
		Cheat.LOGGER.log(Level.FINEST, "Client Reply ignored.");
	}

	@Override
	public void handleServiceDecline(SessionInfo info, ServiceDecline decline) {
		Cheat.LOGGER.log(Level.FINEST, "Client Decline ignored.");
	}

	@Override
	public void sendServiceOk(SessionInfo info, ServiceOk ok) {
		Cheat.LOGGER.log(Level.FINEST, "Server Ok sending ignored.");
	}

	@Override
	public void sendServiceRequest(SessionInfo info, ServiceRequest request) {
		Cheat.LOGGER.log(Level.FINEST, "Server Request sending ignored.");
	}
	
}
