package server;

import java.io.IOException;

import util.SerializerBuffer;

public interface NetworkWriter {
	public int write(ClientIdentifier clientId, SerializerBuffer serializerBuffer) throws IOException;
}
