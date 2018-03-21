package client;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractUDPNetworkHandler;
import util.Cheat;
import util.SerializerBuffer;

public class ClientUDPNetworkHandler extends AbstractUDPNetworkHandler {

	private final ClientMessageHandler messageHandler;
	private ExecutorService executor;
	
	private DatagramChannel channel;
	
	public ClientUDPNetworkHandler(DatagramChannel channel, int op, Client client) throws IOException {
		super(channel, SelectionKey.OP_READ, client);
		this.messageHandler = new ClientMessageHandler(getSerializerBuffer(channel), client);
		this.channel = channel;
		this.executor = Executors.newSingleThreadExecutor();
		executor.execute(messageHandler);
	}	
	
	public int write(SerializerBuffer serializerBuffer) throws IOException {
		if(channel == null)
			throw new NullPointerException("Excepting a non-null SocketChannel.");
		return serializerBuffer.write(channel);
	}
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		messageHandler.shutdown();
		Cheat.LOGGER.log(Level.INFO, "Server disconnected.");
	}

}