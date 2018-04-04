package service.server;

import protocol.Message;
import service.message.ServiceDecline;
import service.message.ServiceOk;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import service.message.ServiceStart;
import service.message.ServiceStop;
import session.SessionInfo;

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
public interface ServiceServerProtocolHandler {
	
	/**
	 * Handles a {@link ServiceStart} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param startService The message to handle.
	 */
	void handleServiceStart(SessionInfo info, ServiceStart startService);
	
	/**
	 * Handles a {@link ServiceStop} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param stopService The message to handle.
	 */
	void handleServiceStop(SessionInfo info, ServiceStop stopService);
	
	/**
	 * Handles a {@link ServiceReply} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param reply The message to handle.
	 */
	void handleServiceReply(SessionInfo info, ServiceReply reply);
	
	/**
	 * Handles a {@link ServiceDecline} message from the specified {@link ClientIdentifier}.
	 * @param clientId The client that sent the message.
	 * @param decline The message to handle.
	 */
	void handleServiceDecline(SessionInfo info, ServiceDecline decline);
	
	/**
	 * Sends an {@link ServiceOk} message tp the specified {@link ClientIdentifier}.
	 * @param clientId The client to send the message to.
	 * @param ok The message to send.
	 */
	void sendServiceOk(SessionInfo info, ServiceOk ok);
	
	/**
	 * Sends a {@link ServiceRequest} message tp the specified {@link ClientIdentifier}.
	 * @param clientId The client to send the message to.
	 * @param request The message to send.
	 */
	void sendServiceRequest(SessionInfo info, ServiceRequest request);
}
