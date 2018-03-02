package client;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Client extends Runnable {
	public SocketChannel connect(String hostname, int port) throws IOException;
	public boolean isConnected();
	public void shutdown();
	public void addHandler(AbstractHandler handler);
	public void removeHandler(AbstractHandler handler);
}