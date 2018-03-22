package server;

/**
 * {@code ServerNetworkHandler} interface extends the {@link NetworkWriter}
 * by providing a way to update a {@link ClientIdentifier}.<br />
 * This feature is used when a new client is connected but not yet registered
 * to the server.
 * @see NetworkWriter
 * @see ClientIdentifier
 */
public interface ServerNetworkHandler extends NetworkWriter {
	
	/**
	 * Replaces the oldClientId by the newClientId.<br />
	 * This method should be used when a new client is connected but not yet registered
	 * to the server. Therefore the old default-clientId can be replaced by a valid one.
	 * @param oldClientId The old clientId to remove.
	 * @param newClientId The new clientId to add.
	 */
	void update(ClientIdentifier oldClientId, ClientIdentifier newClientId);
}
