package client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import io.AbstractNetworkHandler;
import io.SerializerBuffer;
import protocol.Abort;
import protocol.Flag;
import protocol.Ok;
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
			case Flag.OK: handleOk(channel,serializerBuffer); break;
			case Flag.REQUEST: handleRequest(channel, serializerBuffer); break;
			case Flag.ABORT: handleAbort(channel, serializerBuffer); break;
			default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
		}
	}
	
	private void handleOk(SocketChannel channel, SerializerBuffer serializerBuffer) {
		handleMessage(serializerBuffer, Ok.CREATOR, client::handleOk);	
	}
	
	private void handleRequest(SocketChannel channel, SerializerBuffer serializerBuffer) {
		handleMessage(serializerBuffer, Request.CREATOR, client::handleRequest);
	}
	
	private void handleAbort(SocketChannel channel, SerializerBuffer serializerBuffer) {
		handleMessage(serializerBuffer, Abort.CREATOR, client::handleAbort);
	}

}