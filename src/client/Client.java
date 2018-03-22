package client;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import io.IOEntity;
import client.NetworkWriter;

public interface Client extends IOEntity, ClientProtocolHandler, NetworkWriter {
	void connect(String hostname, int port) throws IOException;
	void scan (String hostname);
	void setProtocolHandler(ClientProtocolHandler protocolHandler);
	int getNbProcessUnits();
	int getNbTaskMax();
	void setId(long id);
	long getId();
}