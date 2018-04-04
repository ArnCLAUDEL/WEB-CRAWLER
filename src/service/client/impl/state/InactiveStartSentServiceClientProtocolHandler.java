package service.client.impl.state;

import java.net.SocketAddress;
import java.util.TimerTask;

import protocol.NetworkWriter;
import service.client.ServiceClient;
import service.message.ServiceDecline;
import service.message.ServiceOk;
import service.message.ServiceRequest;
import service.message.ServiceStart;

public class InactiveStartSentServiceClientProtocolHandler extends AbstractServiceClientProtocolHandler {
	
	private final TimerTask retry;
	
	public InactiveStartSentServiceClientProtocolHandler(NetworkWriter writer, ServiceClient client, ServiceStart startService, SocketAddress to) {
		super(writer, client);
		retry = retry(startService, start -> sendServiceStart(to, start));
		schedule(retry, DEFAULT_FIRST_TIME_RETRY, DEFAULT_PERIOD_RETRY);
	}
	
	@Override
	public void sendServiceStart(SocketAddress from, ServiceStart startService) {
		send(from, startService);
	}
	
	@Override
	public void handleServiceOk(SocketAddress from, ServiceOk ok) {
		retry.cancel();
		client.setProtocolHandler(new ActiveServiceClientProtocolHandler(networkWriter.get(), client));
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
		return "INACTIVE_START_SENT";
	}
	
}
