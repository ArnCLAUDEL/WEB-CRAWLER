package service.server.impl.state;

import java.util.logging.Level;

import protocol.NetworkWriter;
import service.message.ServiceDecline;
import service.message.ServiceOk;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import service.message.ServiceStart;
import service.message.ServiceStop;
import service.server.ServiceServer;
import session.SessionInfo;
import util.Cheat;

public class ActiveServiceServerProtocolHandler extends AbstractServiceServerProtocolHandler {

	public ActiveServiceServerProtocolHandler(NetworkWriter writer, ServiceServer server) {
		super(writer, server);
	}
	
	@Override
	public void sendServiceOk(SessionInfo info, ServiceOk ok) {
		send(info.getCertificate().getAddress(), ok);
	}
	
	@Override
	public void sendServiceRequest(SessionInfo info, ServiceRequest request) { 
		send(info.getCertificate().getAddress(), request);
	}

	@Override
	public void handleServiceStart(SessionInfo info, ServiceStart startService) {
		server.setClientActivity(info, true);
		send(info.getCertificate().getAddress(), new ServiceOk());
	}
	
	@Override
	public void handleServiceStop(SessionInfo info, ServiceStop stopService) {
		server.setClientActivity(info, false);
	}

	@Override
	public void handleServiceReply(SessionInfo info, ServiceReply reply) {
		server.processReply(reply);
	}
	
	@Override
	public void handleServiceDecline(SessionInfo info, ServiceDecline decline) {
		// TODO 
		Cheat.LOGGER.log(Level.INFO, "Client Decline handled. (Not yet implemented)");
	}

	@Override
	public String toString() {
		return "ACTIVE";
	}
}
