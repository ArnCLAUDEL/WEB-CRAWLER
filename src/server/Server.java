package server;

import io.IOEntity;
import protocol.Reply;
import protocol.Request;

public interface Server extends IOEntity, ServerProtocolHandler, NetworkWriter {	
	boolean addClient(ClientIdentifier clientId);
	void removeClient(ClientIdentifier clientId);
	void setClientActivity(ClientIdentifier clientId, boolean active);
	void scan(String hostname);
	boolean sendRequest(Request request);
	void processReply(Reply reply);
	void update(ClientIdentifier oldClientId, ClientIdentifier newClientId);
}
