package client;

import protocol.Reply;
import protocol.Request;

public interface ClientProtocolHandler {
	void sendInit();
	void sendStartService();
	void sendStopService();
	void sendReply(Reply reply);
	void sendDecline(Request request);
	void sendForget();
	void handleRequest(Request request);
	void handleOk();
	void handleAbort(Request request);
}