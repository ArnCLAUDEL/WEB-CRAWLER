package app.cass;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.AbstractUDPNetworkHandler;
import protocol.NetworkWriter;
import util.SerializerBuffer;

public class CASSUDPNetworkHandler extends AbstractUDPNetworkHandler implements NetworkWriter {

	private final Executor executor;
	private final CASS cass;
	
	public CASSUDPNetworkHandler(DatagramChannel channel, CASS cass) throws IOException {
		super(channel, SelectionKey.OP_READ, cass);
		this.cass = cass;
		this.executor = Executors.newCachedThreadPool();
	}

	@Override
	protected void register(SocketAddress address, SerializerBuffer serializerBuffer) {
		super.register(address, serializerBuffer);
		CASSMessageHandler handler = new CASSMessageHandler(serializerBuffer, address, cass, cass);
		executor.execute(handler);
	}

	@Override
	public int write(SocketAddress address, SerializerBuffer buffer) throws IOException {
		return send(address, buffer);
	}	
	
}
