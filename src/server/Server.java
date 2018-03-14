package server;

import io.IOEntity;
import protocol.ClientIdentifier;
import protocol.Reply;

public interface Server extends IOEntity, ServerProtocolHandler {	
	boolean addClient(ClientIdentifier clientId);
	void removeClient(ClientIdentifier clientId);
	void setClientActivity(ClientIdentifier clientId, boolean active);
	void scan(String hostname);
}
