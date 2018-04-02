package server.impl;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

import server.impl.state.ActiveServerProtocolHandler;

public abstract class AbstractTCPServer extends AbstractServer {

	private ServerSocketChannel serverSocket;
	
	public AbstractTCPServer(int port) {
		super(port);
		
	}
	
	@Override
	protected void start() throws IOException {
		serverSocket = ServerSocketChannel.open();
		serverSocket.bind(address);
		active = true;
		startHandlers();
		this.protocolHandler = new ActiveServerProtocolHandler(this);
	}
	
	private void startHandlers() throws IOException {
		this.networkHandler = new ServerTCPNetworkHandler(serverSocket, this);
		addHandler(networkHandler);
		addHandler(new ServerKeyboardHandler(this));
	}

}
