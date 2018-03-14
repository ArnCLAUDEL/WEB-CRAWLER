package client.state;

import java.nio.channels.SocketChannel;

import client.Client;
import protocol.Ok;

public class InactiveStartSentClientProtocolHandler extends InactiveClientProtocolHandler {
	
	public InactiveStartSentClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}
	
	@Override
	public void handleOk(Ok ok) {
		client.setProtocolHandler(new ActiveClientProtocolHandler(client, channel));
	}
	
	@Override
	public String toString() {
		return "Inactive Start Sent";
	}
	
}
