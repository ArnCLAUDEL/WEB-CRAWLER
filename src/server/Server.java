package server;

import io.IOEntity;
import protocol.ClientIdentifier;
import protocol.Reply;
import protocol.ServerProtocolHandler;

public interface Server extends IOEntity, ServerProtocolHandler {	
	boolean addClient(ClientIdentifier clientId);
	void removeClient(ClientIdentifier clientId);
	void setClientActivity(ClientIdentifier clientId, boolean active);
}
