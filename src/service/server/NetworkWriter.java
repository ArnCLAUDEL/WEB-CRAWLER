package service.server;

import java.io.IOException;

import io.NetworkHandler;
import util.SerializerBuffer;

/**
 * {@code NetworkWriter} interface extends {@link NetworkHandler}
 * by providing a way for a server to send data into the network to a client.<br />
 */
public interface NetworkWriter extends NetworkHandler {
	
	/**
	 * Sends the data contained in the {@links SerializerBuffer} to
	 * the specified client. 
	 * @param buffer The buffer that contains the data.
	 * @param clientId The client to send the data to.
	 * @return The number of bytes written.
	 * @throws IOException If a I/O error occurs.
	 */
	public int write(ClientIdentifier clientId, SerializerBuffer serializerBuffer) throws IOException;
}
