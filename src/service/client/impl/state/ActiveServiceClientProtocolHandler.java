package service.client.impl.state;

import java.net.SocketAddress;

import protocol.NetworkWriter;
import service.client.ServiceClient;
import service.message.ServiceDecline;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import service.message.ServiceStop;

public class ActiveServiceClientProtocolHandler extends AbstractServiceClientProtocolHandler {
	
	public ActiveServiceClientProtocolHandler(NetworkWriter writer, ServiceClient client) {
		super(writer, client);
	}
	
	@Override
	public void sendServiceReply(SocketAddress to, ServiceReply reply) {
		serializerBuffer.setUnderflowCallback(getFlushCallback(to));
		send(to, reply);
		serializerBuffer.setUnderflowCallback(null);
	}
	
	@Override
	public void sendServiceDecline(SocketAddress to, ServiceDecline decline) {
		send(to, decline);
	}
	
	@Override
	public void sendServiceStop(SocketAddress to, ServiceStop stopService) {
		send(to, stopService);
		client.setProtocolHandler(new InactiveServiceClientProtocolHandler(networkWriter.get(), client));
	}
	
	@Override
	public void handleServiceRequest(SocketAddress from, ServiceRequest request) {
		client.scan(request.getHostname(),request.getLink());	
	}
	
	@Override
	public String toString() {
		return "ACTIVE";
	}
	
}
