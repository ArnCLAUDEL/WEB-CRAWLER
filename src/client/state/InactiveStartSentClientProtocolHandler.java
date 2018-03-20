package client.state;

import java.util.TimerTask;

import client.Client;
import protocol.Decline;
import protocol.Ok;
import protocol.Request;
import protocol.StartService;

public class InactiveStartSentClientProtocolHandler extends AbstractClientProtocolHandler {
	
	private final TimerTask retry;
	
	public InactiveStartSentClientProtocolHandler(Client client, StartService startService) {
		super(client);
		retry = retry(startService, this::sendStartService);
		schedule(retry, DEFAULT_FIRST_TIME_RETRY, DEFAULT_PERIOD_RETRY);
	}
	
	@Override
	public void sendStartService(StartService startService) {
		send(startService);
	}
	
	@Override
	public void handleOk(Ok ok) {
		retry.cancel();
		client.setProtocolHandler(new ActiveClientProtocolHandler(client));
	}
	
	@Override
	public void handleRequest(Request request) {
		sendDecline(new Decline(request));
	}
	
	@Override
	public void sendDecline(Decline decline) {
		send(decline);
	}
	
	@Override
	public String toString() {
		return "INACTIVE_START_SENT";
	}
	
}
