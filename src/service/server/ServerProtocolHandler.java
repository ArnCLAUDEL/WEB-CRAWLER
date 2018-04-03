package service.server;

import protocol.Message;
import service.message.Abort;
import service.message.Decline;
import service.message.Forget;
import service.message.Init;
import service.message.Ok;
import service.message.Reply;
import service.message.Request;
import service.message.StartService;
import service.message.StopService;

/**
 * {@code ServerProtocolHandler} interface represents a object
 * that can handle each, server-side, part of the protocol.<br />
 * Each method <tt>sendMessage(ClientIdentifier, Message)</tt> will serialize and send
 * the message to the specified client.<br />
 * Each method <tt>handleMessage(message)</tt> will handle the received message
 * and eventually will change the server-state or generate the response.<br />
 * @see Message
 * @see ClientIdentifier
 */
public interface ServerProtocolHandler {
	
	/**
	 * Handles an {@link Init} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param init The message to handle.
	 * @return <tt>true</tt> if the init message is valid and the client is registered.
	 */
	boolean handleInit(ClientIdentifier clientId, Init init);
	
	/**
	 * Handles a {@link StartService} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param startService The message to handle.
	 */
	void handleStartService(ClientIdentifier clientId, StartService startService);
	
	/**
	 * Handles a {@link StopService} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param stopService The message to handle.
	 */
	void handleStopService(ClientIdentifier clientId, StopService stopService);
	
	/**
	 * Handles a {@link Reply} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param reply The message to handle.
	 */
	void handleReply(ClientIdentifier clientId, Reply reply);
	
	/**
	 * Handles a {@link Forget} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param forget The message to handle.
	 */
	void handleForget(ClientIdentifier clientId, Forget forget);
	
	/**
	 * Handles a {@link Decline} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param decline The message to handle.
	 */
	void handleDecline(ClientIdentifier clientId, Decline decline);
	
	/**
	 * Sends an {@link Ok} message tp the specified {@link ClientIdentifier}.
	 * @param clientId The client to send the message to.
	 * @param ok The message to send.
	 */
	void sendOk(ClientIdentifier clientId, Ok ok);
	
	/**
	 * Sends an {@link Abort} message tp the specified {@link ClientIdentifier}.
	 * @param clientId The client to send the message to.
	 * @param abort The message to send.
	 */
	void sendAbort(ClientIdentifier clientId, Abort abort);
	
	/**
	 * Sends a {@link Request} message tp the specified {@link ClientIdentifier}.
	 * @param clientId The client to send the message to.
	 * @param request The message to send.
	 */
	void sendRequest(ClientIdentifier clientId, Request request);
}
