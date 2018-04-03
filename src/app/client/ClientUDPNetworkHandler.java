package app.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.AbstractUDPNetworkHandler;
import protocol.NetworkWriter;
import util.SerializerBuffer;

public class ClientUDPNetworkHandler extends AbstractUDPNetworkHandler implements NetworkWriter {

	private final Executor executor;
	private final Client client;
	
	public ClientUDPNetworkHandler(DatagramChannel channel, Client client) throws IOException {
		super(channel, SelectionKey.OP_READ, client);
		this.executor = Executors.newCachedThreadPool();
		this.client = client;
	}
	
	@Override
	protected void register(SocketAddress address, SerializerBuffer serializerBuffer) {
		super.register(address, serializerBuffer);
		ClientMessageHandler handler = new ClientMessageHandler(serializerBuffer, address, client, client, client);
		executor.execute(handler);
	}

	@Override
	public int write(SocketAddress address, SerializerBuffer buffer) throws IOException {
		return send(address, buffer);
	}

}
