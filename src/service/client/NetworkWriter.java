package service.client;

import java.io.IOException;

import io.NetworkHandler;
import util.SerializerBuffer;

/**
 * {@code NetworkWriter} interface extends {@link NetworkHandler}
 * by providing a way for a client to send data into the network to his server.<br />
 * It's assumed that there is only one server so there is no need of destination.
 * @see SerializerBuffer
 * @see NetworkHandler
 */
public interface NetworkWriter extends NetworkHandler {
	
	/**
	 * Sends the data contained in the {@links SerializerBuffer} to
	 * the network. 
	 * @param buffer The buffer that contains the data.
	 * @return The number of bytes written.
	 * @throws IOException If a I/O error occurs.
	 */
	public int write(SerializerBuffer buffer) throws IOException;
}
