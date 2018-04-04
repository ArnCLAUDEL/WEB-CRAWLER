package service.client.impl.state;

import java.net.SocketAddress;

import protocol.NetworkWriter;
import service.client.ServiceClient;
import service.message.ServiceDecline;
import service.message.ServiceRequest;
import service.message.ServiceStart;

public class InactiveServiceClientProtocolHandler extends AbstractServiceClientProtocolHandler {
	
	public InactiveServiceClientProtocolHandler(NetworkWriter writer, ServiceClient client) {
		super(writer, client);
	}
	
	@Override
	public void sendServiceStart(SocketAddress to, ServiceStart startService) {
		send(to, startService);
		client.setProtocolHandler(new InactiveStartSentServiceClientProtocolHandler(networkWriter.get(), client, startService, to));
	}
	
	@Override
	public void handleServiceRequest(SocketAddress from, ServiceRequest request) {
		sendServiceDecline(from, new ServiceDecline(request));
	}
	
	@Override
	public void sendServiceDecline(SocketAddress to, ServiceDecline decline) {
		send(to, decline);
	}
	
	@Override
	public String toString() {
		return "INACTIVE";
	}
	
}
