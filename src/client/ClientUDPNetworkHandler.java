package client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractUDPNetworkHandler;
import util.Cheat;
import util.SerializerBuffer;

public class ClientUDPNetworkHandler extends AbstractUDPNetworkHandler implements ClientNetworkHandler {

	private final ClientMessageHandler messageHandler;
	private ExecutorService executor;
	
	private DatagramChannel channel;
	
	public ClientUDPNetworkHandler(DatagramChannel channel, Client client) throws IOException {
		super(channel, SelectionKey.OP_READ, client);
		register(channel.getRemoteAddress(), getSerializerBuffer(channel));
		this.messageHandler = new ClientMessageHandler(getSerializerBuffer(channel), client);
		this.channel = channel;
		this.executor = Executors.newSingleThreadExecutor();
		executor.execute(messageHandler);
	}	
	
	@Override
	public int write(SerializerBuffer serializerBuffer) throws IOException {
		if(channel == null)
			throw new NullPointerException("Excepting a non-null SocketChannel.");
		return send(channel.getRemoteAddress(), serializerBuffer);
	}
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		messageHandler.shutdown();
		Cheat.LOGGER.log(Level.INFO, "Server disconnected.");
	}

	@Override
	protected void handleReadOperation(SelectionKey sk, SerializerBuffer serializerBuffer) {
		Cheat.LOGGER.log(Level.FINER, "Handling READ_OPERATION.");
		DatagramChannel channel = (DatagramChannel) sk.channel();
		try {
			synchronized (serializerBuffer) {
				serializerBuffer.compact();
				serializerBuffer.receive(channel);
				serializerBuffer.flip();
				serializerBuffer.notifyAll();
			}
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Error while handling READ_OPERATION.", e);
		}
	}
	
}