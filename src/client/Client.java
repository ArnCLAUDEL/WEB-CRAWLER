package client;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import io.IOEntity;

public interface Client extends IOEntity, ClientProtocolHandler  {
	SocketChannel connect(String hostname, int port) throws IOException;
	void setProtocolHandler(ClientProtocolHandler protocolHandler);
	int getNbProcessUnits();
	int getNbTaskMax();
}