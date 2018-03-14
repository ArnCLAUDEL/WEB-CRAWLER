package server;

import java.net.InetSocketAddress;

import io.AbstractIOEntity;
import util.Cheat;

public abstract class AbstractServer extends AbstractIOEntity implements Server {
	
	protected final int port;
	protected final InetSocketAddress address;
	
	public AbstractServer(int port) {
		super("Server " + Cheat.getId());
		this.port = port;
		this.address = new InetSocketAddress(port);
	}
}
