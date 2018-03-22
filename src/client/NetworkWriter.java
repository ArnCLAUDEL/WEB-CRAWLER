package client;

import java.io.IOException;

import io.NetworkHandler;
import util.SerializerBuffer;

public interface NetworkWriter extends NetworkHandler {
	public int write(SerializerBuffer buffer) throws IOException;
}
