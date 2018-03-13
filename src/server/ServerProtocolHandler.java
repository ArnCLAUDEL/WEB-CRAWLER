package server;

import protocol.ClientIdentifier;
import protocol.Reply;
import protocol.Request;

public interface ServerProtocolHandler {
	boolean handleInit(ClientIdentifier clientId);
	void handleStartService(ClientIdentifier clientId);
	void handleStopService(ClientIdentifier clientId);
	void handleReply(ClientIdentifier clientId, Reply reply);
	void handleForget(ClientIdentifier clientId);
	void handleDecline(ClientIdentifier clientId, Request request);
	void sendOk(ClientIdentifier clientId);
	void sendAbort(ClientIdentifier clientId, Request request);
	void sendRequest(ClientIdentifier clientId, Request request);
}
