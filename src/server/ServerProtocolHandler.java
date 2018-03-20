package server;

import protocol.Abort;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.StartService;
import protocol.StopService;

public interface ServerProtocolHandler {
	boolean handleInit(ClientIdentifier clientId, Init init);
	void handleStartService(ClientIdentifier clientId, StartService startService);
	void handleStopService(ClientIdentifier clientId, StopService stopService);
	void handleReply(ClientIdentifier clientId, Reply reply);
	void handleForget(ClientIdentifier clientId, Forget forget);
	void handleDecline(ClientIdentifier clientId, Decline decline);
	void sendOk(ClientIdentifier clientId, Ok ok);
	void sendAbort(ClientIdentifier clientId, Abort abort);
	void sendRequest(ClientIdentifier clientId, Request request);
}
