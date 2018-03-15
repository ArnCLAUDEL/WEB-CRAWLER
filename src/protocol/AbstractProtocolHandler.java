package protocol;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import io.SerializerBuffer;
import util.Cheat;

public class AbstractProtocolHandler {
	
	private final SerializerBuffer serializerBuffer;
	
	public AbstractProtocolHandler() {
		this.serializerBuffer = new SerializerBuffer();
	}
	
	private boolean send(SocketChannel channel) {
		try {
			serializerBuffer.write(channel);
			return true;
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Failed to send message.", e);
			return false;
		}
	}
	
	protected synchronized boolean send(SocketChannel channel, Message message) {
		serializerBuffer.clear();
		serializerBuffer.put(message.getFlag());
		message.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send(channel)) {
			Cheat.LOGGER.log(Level.FINER, message + " sent.");
			return true;
		}
		return false;
	}
	
}
