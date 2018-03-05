package client;

import java.nio.channels.Channels;

import io.AbstractRawInputHandler;

public class ClientKeyboardHandler extends AbstractRawInputHandler{

	public ClientKeyboardHandler() {
		super(Channels.newChannel(System.in));
	}
	
	@Override
	public String toString() {
		return "Keyboard Handler " + Thread.currentThread().getId();
	}

}
