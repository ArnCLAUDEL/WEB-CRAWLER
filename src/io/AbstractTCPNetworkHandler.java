package io;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import util.Cheat;
import util.SerializerBuffer;

public abstract class AbstractTCPNetworkHandler extends AbstractNetworkHandler {
	
	public AbstractTCPNetworkHandler(SelectableChannel channel, int op, IOEntity entity) throws IOException {
		super(channel, op, entity);		
	}	
	
	protected int write(SocketChannel channel, SerializerBuffer buffer) throws IOException {
		if(channel == null)
			throw new NullPointerException("Excepting a non-null channel.");
		
		return channel.write(buffer.getBuffer());
	}
	
	protected void register(SocketChannel channel, SerializerBuffer buffer) {}
	
	@Override
	protected void handleAcceptOperation(SelectionKey sk) {
		Cheat.LOGGER.log(Level.FINER, "Handling ACCEPT_OPERATION..");
		try {
			SocketChannel socket = ((ServerSocketChannel) sk.channel()).accept();
			addChannel(socket, SelectionKey.OP_READ);
			SerializerBuffer serializerBuffer = getSerializerBuffer(socket);
			register(socket, serializerBuffer);
			Cheat.LOGGER.log(Level.INFO,"Connection from " + socket.getRemoteAddress() + " accepted");
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Error while handling ACCEPT_OPERATION.", e);
		}
	}

	@Override
	protected void handleReadOperation(SelectionKey sk, SerializerBuffer serializerBuffer) {
		Cheat.LOGGER.log(Level.FINER, "Handling READ_OPERATION..");
		SocketChannel sc = (SocketChannel) sk.channel();
		try {
			synchronized (serializerBuffer) {
				serializerBuffer.compact();
				int read = serializerBuffer.read(sc);
				if(read < 0) {
					channelClosedCallback(sk);
				}
				serializerBuffer.flip();
				serializerBuffer.notifyAll();
			}
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Error while handling READ_OPERATION.", e);
		}
	}
	
	@Override
	protected void handleConnectOperation(SelectionKey sk) {};
	
	@Override
	protected void handleWriteOperation(SelectionKey sk, SerializerBuffer serializerBuffer) {};
}
