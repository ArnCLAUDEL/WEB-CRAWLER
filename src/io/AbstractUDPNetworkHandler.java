package io;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import util.Cheat;
import util.SerializerBuffer;

public abstract class AbstractUDPNetworkHandler extends AbstractNetworkHandler {

	private final Map<SocketAddress, SerializerBuffer> buffers;
	private final DatagramChannel channel;
	
	public AbstractUDPNetworkHandler(DatagramChannel channel, int op, IOEntity ioEntity) throws IOException {
		super(channel, op, ioEntity);
		this.channel = channel;
		this.buffers = new HashMap<>();
	}
	
	private boolean checkRegistery(SocketAddress address) {
		if(address == null)
			return false;
		if(!buffers.containsKey(address)) {
			SerializerBuffer serializerBuffer = new SerializerBuffer();
			buffers.put(address, serializerBuffer);
			register(address, serializerBuffer);
		}
		return true;
	}
	
	protected void register(SocketAddress address, SerializerBuffer serializerBuffer) {
		this.buffers.put(address, serializerBuffer);
	}
	
	protected SerializerBuffer getSerializerBuffer(SocketAddress address) {
		return buffers.get(address);
	}
	
	protected int send(SocketAddress address, SerializerBuffer serializerBuffer) throws IOException {
		return serializerBuffer.send(channel, address);
	}

	@Override
	protected void handleReadOperation(SelectionKey sk, SerializerBuffer serializerBuffer) {
		Cheat.LOGGER.log(Level.FINER, "Handling READ_OPERATION.");
		DatagramChannel channel = (DatagramChannel) sk.channel();
		try {
			serializerBuffer.clear();
			SocketAddress addressFrom = serializerBuffer.receive(channel);
			if(!checkRegistery(addressFrom))
				return;
			serializerBuffer.flip();
			SerializerBuffer peerSerializerBuffer = getSerializerBuffer(addressFrom);
			synchronized (peerSerializerBuffer) {
				peerSerializerBuffer.compact();
				peerSerializerBuffer.put(serializerBuffer);
				peerSerializerBuffer.flip();
				peerSerializerBuffer.notifyAll();
			}
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Error while handling READ_OPERATION.", e);
		}
	}
	
	@Override
	protected void handleConnectOperation(SelectionKey sk) {}
	
	@Override
	protected void handleAcceptOperation(SelectionKey sk) {}
	
	@Override
	protected void handleWriteOperation(SelectionKey sk, SerializerBuffer serializerBuffer) {}
	
}
