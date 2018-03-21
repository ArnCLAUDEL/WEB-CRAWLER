package server;

import java.io.IOException;

import io.NetworkHandler;
import util.SerializerBuffer;

public interface NetworkWriter extends NetworkHandler {
	public int write(ClientIdentifier clientId, SerializerBuffer serializerBuffer) throws IOException;
}
