package app.service.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractTCPNetworkHandler;
import io.Handler;
import protocol.NetworkWriter;
import util.BiMap;
import util.Cheat;
import util.SerializerBuffer;

public class ClientTCPNetworkHandler extends AbstractTCPNetworkHandler implements NetworkWriter {

	private final Executor executor;
	private final Client client;
	private final BiMap<SocketChannel, SocketAddress> channelsAddress;
	private final Map<SocketChannel, ClientMessageHandler> handlers;
	
	public ClientTCPNetworkHandler(ServerSocketChannel channel, Client client) throws IOException {
		super(channel, SelectionKey.OP_ACCEPT, client);
		this.client = client;
		this.executor = Executors.newCachedThreadPool();
		this.handlers = new HashMap<>();
		this.channelsAddress = new BiMap<>();
	}
	
	public void connect(SocketAddress address) throws IOException {
		SocketChannel channel = SocketChannel.open(address);
		addChannel(channel, SelectionKey.OP_READ);
		SerializerBuffer serializerBuffer = getSerializerBuffer(channel);
		register(channel, serializerBuffer);
		Cheat.LOGGER.log(Level.INFO, "Connected to " + address);
	}
	
	@Override
	protected void register(SocketChannel channel, SerializerBuffer buffer) {
		super.register(channel, buffer);
		try {
			channelsAddress.put(channel, channel.getRemoteAddress());
			ClientMessageHandler handler = new ClientMessageHandler(buffer, channel.getRemoteAddress(), client, client, client);
			handlers.put(channel, handler);
			executor.execute(handler);
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Failed to register the SocketChannel", e);
		}
	}
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		Handler handler = handlers.remove(sk.channel());
		if(handler != null)
			handler.shutdown();
	}

	@Override
	public int write(SocketAddress address, SerializerBuffer buffer) throws IOException {
		return write(channelsAddress.getRight(address), buffer);
	}

	
	
}
