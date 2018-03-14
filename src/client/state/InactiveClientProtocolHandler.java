package client.state;

import java.nio.channels.SocketChannel;

import client.Client;
import protocol.Forget;
import protocol.StartService;

public class InactiveClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public InactiveClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}
	
	@Override
	public void sendStartService(StartService startService) {
		if(send(startService))
			client.setProtocolHandler(new InactiveStartSentClientProtocolHandler(client, channel));
	}
	
	@Override
	public void sendForget(Forget forget) {
		if(send(forget))
			client.setProtocolHandler(new InitClientProtocolHandler(client, channel));
	}
	
	@Override
	public String toString() {
		return "Inactive";
	}
	
}
