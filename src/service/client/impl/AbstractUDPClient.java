package service.client.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

import process.ProcessExecutor;
import service.client.impl.state.InitClientProtocolHandler;
import service.message.Init;

public abstract class AbstractUDPClient extends AbstractClient {

	protected DatagramChannel channel;
	
	public AbstractUDPClient(String hostname, int port, long previousId) {
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
		this.networkHandler = new ClientUDPNetworkHandler(channel, this);
		addHandler(networkHandler);
	}
	
	@Override
	public void connect(String hostname, int port) throws IOException {
		this.channel = DatagramChannel.open(); 
		this.channel.bind(null);
		this.channel.connect(new InetSocketAddress(hostname, port));
		this.connected = true;
	}
	
}
