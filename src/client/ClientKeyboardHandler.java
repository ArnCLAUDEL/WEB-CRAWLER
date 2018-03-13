package client;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import io.AbstractRawInputHandler;
import io.SerializerBuffer;
import util.Cheat;

public class ClientKeyboardHandler extends AbstractRawInputHandler{

	private final SocketChannel channelOUT;
	
	public ClientKeyboardHandler(SocketChannel channelOUT) {
		super(Channels.newChannel(System.in));
		this.channelOUT = channelOUT;
	}
	
	@Override
	protected void handle() {
		try {
			serializerBuffer.clear();
			if(serializerBuffer.read(channel) < 0) {
				shutdown();
				return;
			}
			serializerBuffer.flip();
			serializerBuffer.write(channelOUT);
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	@Override
	public String toString() {
		return "Keyboard Handler " + Thread.currentThread().getId();
	}

}