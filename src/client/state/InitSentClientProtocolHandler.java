package client.state;

import java.nio.channels.SocketChannel;

import client.Client;
import protocol.Decline;
import protocol.Ok;
import protocol.Request;
import protocol.StartService;

public class InitSentClientProtocolHandler extends InitClientProtocolHandler {

	public InitSentClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}

	@Override
	public void handleOk(Ok ok) {
		client.setProtocolHandler(new InactiveClientProtocolHandler(client, channel));
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
		return "Init Sent";
	}
	
}
