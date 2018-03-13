package protocol;

public interface ServerProtocolHandler {
	boolean handleInit(ClientIdentifier clientId);
	void handleStartService(ClientIdentifier clientId);
	void handleStopService(ClientIdentifier clientId);
	void handleReply(ClientIdentifier clientId, Reply reply);
	void handleForget(ClientIdentifier clientId);
}
