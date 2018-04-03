package service.client.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import process.ProcessExecutor;
import service.client.impl.state.InitClientProtocolHandler;
import service.message.Init;

public abstract class AbstractTCPClient extends AbstractClient {

	protected SocketChannel channel;
	
	public AbstractTCPClient(String hostname, int port, long previousId) {
		super(hostname, port, previousId);
	}
	
	@Override
	protected void start() throws IOException {
		connect(hostname, port);
		startHandlers();
		this.protocolHandler = new InitClientProtocolHandler(this);
		this.protocolHandler.sendInit(new Init(getName(), ProcessExecutor.TASK_CAPACITY, ProcessExecutor.THREAD_CAPACITY, id));
	}
	
	private void startHandlers() throws IOException {
		this.networkHandler = new ClientTCPNetworkHandler(channel, this);
		addHandler(networkHandler);
	}
	
	@Override
	public void connect(String hostname, int port) throws IOException {
		this.channel = SocketChannel.open(new InetSocketAddress(hostname, port));
		this.connected = true;
	}

}
