package server;

import java.net.InetSocketAddress;

import io.AbstractIOEntity;

public abstract class AbstractServer extends AbstractIOEntity implements Server {
	
	protected final int port;
	protected final InetSocketAddress address;
	
	public AbstractServer(int port) {
		this.port = port;
		this.address = new InetSocketAddress(port);
	}
}
