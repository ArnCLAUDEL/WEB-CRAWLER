package service.client.impl.state;

import java.util.TimerTask;

import service.client.Client;
import service.message.Decline;
import service.message.Init;
import service.message.Ok;
import service.message.Request;
import service.message.StartService;

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
