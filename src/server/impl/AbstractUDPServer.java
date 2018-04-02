package server.impl;

import java.io.IOException;
import java.nio.channels.DatagramChannel;

import server.impl.state.ActiveServerProtocolHandler;

public abstract class AbstractUDPServer extends AbstractServer {

	private DatagramChannel channel;
	
	public AbstractUDPServer(int port) {
		super(port);
	}
	
	@Override
	protected void start() throws IOException {
		channel = DatagramChannel.open();
		channel.bind(address);
		active = true;
		startHandlers();
		this.protocolHandler = new ActiveServerProtocolHandler(this);
	}
	
	private void startHandlers() throws IOException {
		this.networkHandler = new ServerUDPNetworkHandler(channel, this);
		addHandler(networkHandler);
		addHandler(new ServerKeyboardHandler(this));
	}

}
