package client.state;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import client.Client;
import protocol.StartService;
import util.Cheat;

public class InactiveClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public InactiveClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}
	
	@Override
	public void sendStartService() {
		serializerBuffer.clear();
		StartService startService = new StartService();
		startService.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send())
			client.setProtocolHandler(new InactiveStartSentClientProtocolHandler(client, channel));
	}
	
	@Override
	public String toString() {
		return "Inactive";
	}
	
}
