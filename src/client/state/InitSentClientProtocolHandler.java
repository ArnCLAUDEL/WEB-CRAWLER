package client.state;

import java.nio.channels.SocketChannel;

import client.Client;

public class InitSentClientProtocolHandler extends InitClientProtocolHandler {

	public InitSentClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}

	@Override
	public void handleOk() {
		client.setProtocolHandler(new InactiveClientProtocolHandler(client, channel));
		client.sendStartService();
	}
	
	@Override
	public String toString() {
		return "Init Sent";
	}
	
}
