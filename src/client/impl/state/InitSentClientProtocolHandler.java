package client.impl.state;

import java.util.TimerTask;

import client.Client;
import protocol.message.Decline;
import protocol.message.Init;
import protocol.message.Ok;
import protocol.message.Request;
import protocol.message.StartService;

public class InitSentClientProtocolHandler extends AbstractClientProtocolHandler {

	private final TimerTask retry;
	
	public InitSentClientProtocolHandler(Client client, Init init) {
		super(client);
		retry = retry(init, this::sendInit);
		schedule(retry, DEFAULT_FIRST_TIME_RETRY, DEFAULT_PERIOD_RETRY);
	}

	@Override
	public void sendInit(Init init) {
		send(init);
	}
	
	@Override
	public void handleOk(Ok ok) {
		retry.cancel();
		client.setId(ok.getId());
		client.setProtocolHandler(new InactiveClientProtocolHandler(client));
		client.sendStartService(new StartService());
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
		return "INIT_SENT";
	}
	
}
