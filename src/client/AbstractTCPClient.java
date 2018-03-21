package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import client.state.InitClientProtocolHandler;
import process.ProcessExecutor;
import protocol.Init;
import util.SerializerBuffer;

public abstract class AbstractTCPClient extends AbstractClient {

	protected SocketChannel channel;
	protected ClientTCPNetworkHandler networkHandler;
	
	public AbstractTCPClient(String hostname, int port, long previousId) {
		super(hostname, port, previousId);
	}
	
	@Override
	protected void start() throws IOException {
		this.channel = connect(hostname, port);
		startHandlers();
		this.protocolHandler = new InitClientProtocolHandler(this);
		this.protocolHandler.sendInit(new Init(getName(), ProcessExecutor.TASK_CAPACITY, ProcessExecutor.THREAD_CAPACITY, id));
	}
	
	private void startHandlers() throws IOException {
		this.networkHandler = new ClientTCPNetworkHandler(channel, this);
		addHandler(networkHandler);
		addHandler(new ClientKeyboardHandler(channel));
	}
	
	@Override
	public SocketChannel connect(String hostname, int port) throws IOException {
		SocketChannel sc = SocketChannel.open(new InetSocketAddress(hostname, port));
		this.connected = true;
		return sc;
	}
	
	@Override
	public int write(SerializerBuffer serializerBuffer) throws IOException {
		return networkHandler.write(serializerBuffer);
	}

}
