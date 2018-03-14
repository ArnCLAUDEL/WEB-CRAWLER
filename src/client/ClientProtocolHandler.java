package client;

import protocol.Abort;
import protocol.Decline;
import protocol.Forget;
import protocol.Init;
import protocol.Ok;
import protocol.Reply;
import protocol.Request;
import protocol.StartService;
import protocol.StopService;

public interface ClientProtocolHandler {
	void sendInit(Init init);
	void sendStartService(StartService startService);
	void sendStopService(StopService stopService);
	void sendReply(Reply reply);
	void sendDecline(Decline decline);
	void sendForget(Forget forget);
	void handleRequest(Request request);
	void handleOk(Ok ok);
	void handleAbort(Abort abort);
}