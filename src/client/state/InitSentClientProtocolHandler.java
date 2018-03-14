package client.state;

import java.nio.channels.SocketChannel;

import client.Client;
import protocol.Ok;
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
	public String toString() {
		return "Init Sent";
	}
	
}
