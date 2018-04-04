package service.client.impl.state;

import java.net.SocketAddress;
import java.util.logging.Level;

import protocol.AbstractProtocolHandler;
import protocol.NetworkWriter;
import service.client.ServiceClient;
import service.client.ServiceClientProtocolHandler;
import service.message.ServiceDecline;
import service.message.ServiceOk;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import service.message.ServiceStart;
import service.message.ServiceStop;
import util.Cheat;

public abstract class AbstractServiceClientProtocolHandler extends AbstractProtocolHandler implements ServiceClientProtocolHandler {
			
	protected final ServiceClient client;
	
	protected AbstractServiceClientProtocolHandler(ServiceClient client) {
		super();
		this.client = client;
	}
	
	protected AbstractServiceClientProtocolHandler(NetworkWriter writer, ServiceClient client) {
		super(writer);
		this.client = client;
		// TODO 
		//this.serializerBuffer.setOverflowCallback(getFlushCallback());
	}
		
	@Override
	public void handleServiceRequest(SocketAddress address, ServiceRequest request) {
		Cheat.LOGGER.log(Level.FINEST, request + " ignored.");
	}

	@Override
	public void handleServiceOk(SocketAddress address, ServiceOk ok) {
		Cheat.LOGGER.log(Level.FINEST, ok + " ignored.");
	}
	
	@Override
	public void sendServiceStart(SocketAddress address, ServiceStart startService) {
		Cheat.LOGGER.log(Level.FINEST, startService + " ignored.");
	}

	@Override
	public void sendServiceStop(SocketAddress address, ServiceStop stopService) {
		Cheat.LOGGER.log(Level.FINEST, stopService + " ignored.");
	}

	@Override
	public void sendServiceReply(SocketAddress address, ServiceReply reply) {
		Cheat.LOGGER.log(Level.FINEST, reply + " ignored.");
	}

	@Override
	public void sendServiceDecline(SocketAddress address, ServiceDecline decline) {
		Cheat.LOGGER.log(Level.FINEST, decline + " ignored.");
	}
}
