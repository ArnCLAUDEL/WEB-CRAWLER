package client;

import java.io.IOException;

import util.SerializerBuffer;

public interface NetworkWriter {
	public int write(SerializerBuffer buffer) throws IOException;
}
