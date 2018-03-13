package protocol;

public interface ClientProtocolHandler {
	void handleRequest(Request request);
	void handleOk();
}
