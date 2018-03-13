package client.state;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import client.Client;
import protocol.Init;
import util.Cheat;

public class InitClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public InitClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}

	@Override
	public void sendInit() {
		serializerBuffer.clear();
		Init initMessage = new Init(client.getName(), client.getNbTaskMax(), client.getNbProcessUnits());
		initMessage.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send())
			client.setProtocolHandler(new InitSentClientProtocolHandler(client, channel));
	}
	
	@Override
	public String toString() {
		return "Init";
	}
	
}
