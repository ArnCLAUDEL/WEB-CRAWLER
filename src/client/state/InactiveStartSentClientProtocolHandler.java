package client.state;

import java.nio.channels.SocketChannel;

import client.Client;
import protocol.Decline;
import protocol.Ok;
import protocol.Request;

public class InactiveStartSentClientProtocolHandler extends InactiveClientProtocolHandler {
	
	public InactiveStartSentClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}
	
	@Override
	public void handleOk(Ok ok) {
		client.setProtocolHandler(new ActiveClientProtocolHandler(client, channel));
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
		return "Inactive Start Sent";
	}
	
}
