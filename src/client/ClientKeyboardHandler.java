package client;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

import io.AbstractKeyboardHandler;
import io.SerializerBuffer;

public class ClientKeyboardHandler extends AbstractKeyboardHandler {

	private final SocketChannel channelOUT;
	
	public ClientKeyboardHandler(SocketChannel channelOUT) {
		super();
		this.channelOUT = channelOUT;
	}
	
	@Override
	protected void handle(SerializerBuffer serializerBuffer) throws IOException {
		serializerBuffer.write(channelOUT);
	}
	
	@Override
	public String toString() {
		return "Keyboard Handler " + Thread.currentThread().getId();
	}

}