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

/**
 * {@code ClientProtocolHandler} interface represents a object
 * that can handle each, cliend-side, part of the protocol.<br />
 * Each method <tt>sendMessage(Message)</tt> will serialize and send
 * the message to the server.<br />
 * Each method <tt>handleMessage(message)</tt> will handle the received message
 * and eventually will change the client-state or generate the response.<br />
 * @see Message
 */
public interface ClientProtocolHandler {
	
	/**
	 * Serializes and send an {@link Init} message to the server.
	 * @param init The message to send.
	 */
	void sendInit(Init init);
	
	/**
	 * Serializes and send a {@link StartService} message to the server.
	 * @param startService The message to send.
	 */
	void sendStartService(StartService startService);
	
	/**
	 * Serializes and send a {@link StopService} message to the server.
	 * @param stopService The message to send.
	 */
	void sendStopService(StopService stopService);
	
	/**
	 * Serializes and send a {@link Reply} message to the server.
	 * @param reply The message to send.
	 */
	void sendReply(Reply reply);
	
	/**
	 * Serializes and send a {@link Decline} message to the server.
	 * @param decline The message to send.
	 */
	void sendDecline(Decline decline);
	
	/**
	 * Serializes and send a {@link Forget} message to the server.
	 * @param forget The message to send.
	 */
	void sendForget(Forget forget);
	
	/**
	 * Handles a {@link Request} message from the server.
	 * @param request The message to handle.
	 */
	void handleRequest(Request request);
	
	/**
	 * Handles an {@link Ok} message from the server.
	 * @param ok The message to send.
	 */
	void handleOk(Ok ok);
	
	/**
	 * Handles an {@link Abort} message from the server.
	 * @param abort The message to send.
	 */
	void handleAbort(Abort abort);
}