package client;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.Future;

import io.IOEntity;

public interface Client extends IOEntity, ClientProtocolHandler  {
	SocketChannel connect(String hostname, int port) throws IOException;
	void scan (String hostname);
	void setProtocolHandler(ClientProtocolHandler protocolHandler);
	int getNbProcessUnits();
	int getNbTaskMax();
	void setId(long id);
	long getId();
}