package client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractTCPNetworkHandler;
import util.Cheat;
import util.SerializerBuffer;

public class ClientTCPNetworkHandler extends AbstractTCPNetworkHandler implements ClientNetworkHandler {
	
	private final ClientMessageHandler messageHandler;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private SocketChannel channel;
	
	public ClientTCPNetworkHandler(SocketChannel channel, Client client) throws IOException {
		super(channel, SelectionKey.OP_READ, client);
		this.messageHandler = new ClientMessageHandler(getSerializerBuffer(channel), client);
		this.channel = channel;
		executor.execute(messageHandler);
	}	
	
	@Override
	public int write(SerializerBuffer serializerBuffer) throws IOException {
		if(channel == null)
			throw new NullPointerException("Excepting a non-null SocketChannel.");
		return write(channel, serializerBuffer);
	}
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		messageHandler.shutdown();
		Cheat.LOGGER.log(Level.INFO, "Server disconnected.");
	}

}