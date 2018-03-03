package client;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import io.IOEntity;

public interface Client extends IOEntity {
	public SocketChannel connect(String hostname, int port) throws IOException;
}