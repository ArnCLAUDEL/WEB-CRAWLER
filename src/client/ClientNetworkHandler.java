package client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import io.AbstractNetworkHandler;
import io.SerializerBuffer;
import protocol.Flag;
import protocol.Request;
import util.Cheat;

public class ClientNetworkHandler extends AbstractNetworkHandler {
	
	private final Client client;
	
	public ClientNetworkHandler(SocketChannel channel, Client client) throws IOException {
		super(channel, SelectionKey.OP_READ, client);
		this.client = client;
	}
	
	@Override
	public void handleReadOperation(SelectionKey sk, SerializerBuffer serializerBuffer) {
		serializerBuffer.clear();
		SocketChannel sc = (SocketChannel) sk.channel();
		try {
			if(serializerBuffer.read(sc) < 0) {
				sk.cancel();
				sc.close();
				Cheat.LOGGER.log(Level.INFO, "Server disconnected.");
				return;
			}
			serializerBuffer.flip();
			handleProtocol(sc, serializerBuffer);
		} catch(IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}

	@Override
	protected void handleProtocol(SocketChannel channel, SerializerBuffer serializerBuffer) {
		// TODO Auto-generated method stub
		byte flag = serializerBuffer.get();
		switch(flag) {
			case Flag.OK: client.handleOk(); break;
			case Flag.REQUEST: handleRequest(channel, serializerBuffer); break;
			case Flag.ABORT: handleAbort(channel, serializerBuffer); break;
			default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
		}
	}
	
	private void handleRequest(SocketChannel channel, SerializerBuffer serializerBuffer) {
		// TODO
		// Retrieve data
		// Build a Request
		// client.handleRequest(request);
		Request request = Request.CREATOR.init();
		request.readFromBuff(serializerBuffer);
		client.handleRequest(request);
	}
	
	private void handleAbort(SocketChannel channel, SerializerBuffer serializerBuffer) {
		// TODO
		// Retrieve data
		// Build a Request
		// client.handleAbort(request);
		Cheat.LOGGER.log(Level.WARNING, "Handling Abort (not yet implemented)");
	}

}