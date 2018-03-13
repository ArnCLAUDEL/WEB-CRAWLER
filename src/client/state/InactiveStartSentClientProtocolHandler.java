package client.state;

import client.Client;
import java.nio.channels.SocketChannel;

public class InactiveStartSentClientProtocolHandler extends InactiveClientProtocolHandler {
	
	public InactiveStartSentClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}
	
	@Override
	public void handleOk() {
		client.setProtocolHandler(new ActiveClientProtocolHandler(client, channel));
	}
	
	@Override
	public String toString() {
		return "Inactive Start Sent";
	}
	
}
