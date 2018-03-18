package client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractNetworkHandler;
import util.Cheat;

public class ClientNetworkHandler extends AbstractNetworkHandler {
	
	private final ClientMessageHandler messageHandler;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public ClientNetworkHandler(SocketChannel channel, Client client) throws IOException {
		super(channel, SelectionKey.OP_READ, client);
		this.messageHandler = new ClientMessageHandler(getSerializerBuffer(channel), channel, client);
		executor.execute(messageHandler);
	}	
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		Cheat.LOGGER.log(Level.INFO, "Server disconnected.");
	}

}