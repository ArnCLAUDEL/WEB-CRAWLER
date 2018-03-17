package protocol;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.SerializerBuffer;
import util.Cheat;

public class AbstractProtocolHandler {
	
	protected final SerializerBuffer serializerBuffer;
	
	public AbstractProtocolHandler() {
		this.serializerBuffer = new SerializerBuffer();
	}
	
	protected boolean send(SerializerBuffer serializerBuffer, WritableByteChannel channel) {
		try {
			serializerBuffer.write(channel);
			return true;
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Fail to send message.", e);
			return false;
		}
	}
	
	// TODO 
	protected Consumer<? super SerializerBuffer> getFlushCallback(WritableByteChannel channel) {
		return (serializerBuffer) -> {
			serializerBuffer.flip();
			send(serializerBuffer, channel);
			serializerBuffer.clear();
		};
	}
	
	protected boolean send(WritableByteChannel channel) {
		return send(serializerBuffer, channel);
	}
	
	protected synchronized boolean send(WritableByteChannel channel, Message message) {
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
