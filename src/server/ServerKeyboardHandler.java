package server;

import java.io.IOException;

import io.AbstractKeyboardHandler;
import io.SerializerBuffer;
import util.Cheat;

public class ServerKeyboardHandler extends AbstractKeyboardHandler {

	private final Server server;
	
	public ServerKeyboardHandler(Server server) {
		super();
		this.server = server;
	}
	
	@Override
	protected void handle(SerializerBuffer serializerBuffer) throws IOException {
		String hostname = Cheat.CHARSET.decode(serializerBuffer.getBuffer()).toString();
		
		
		server.scan(hostname);
	}
	
	@Override
	public String toString() {
		return "Keyboard Handler " + Thread.currentThread().getId();
	}

}