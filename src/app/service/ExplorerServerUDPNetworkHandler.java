package app.service;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.AbstractUDPNetworkHandler;
import protocol.NetworkWriter;
import util.SerializerBuffer;

public class ExplorerServerUDPNetworkHandler extends AbstractUDPNetworkHandler implements NetworkWriter {

	private final Executor executor;
	private final ExplorerServer explorer; 
	
	public ExplorerServerUDPNetworkHandler(DatagramChannel channel, ExplorerServer explorer) throws IOException {
		super(channel, SelectionKey.OP_READ, explorer);
		this.executor = Executors.newCachedThreadPool();
		this.explorer = explorer;
	}

	@Override
	protected void register(SocketAddress address, SerializerBuffer serializerBuffer) {
		super.register(address, serializerBuffer);
		ExplorerServerMessageHandler handler = new ExplorerServerMessageHandler(serializerBuffer, address, explorer, explorer, explorer);
		executor.execute(handler);
	}
	
	@Override
	public int write(SocketAddress address, SerializerBuffer serializerBuffer) throws IOException {
		return send(address, serializerBuffer);
	}
	
}
