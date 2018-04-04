package service.client;

import java.net.SocketAddress;

import protocol.Message;
import service.message.ServiceDecline;
import service.message.ServiceOk;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import service.message.ServiceStart;
import service.message.ServiceStop;

/**
 * {@code ClientProtocolHandler} interface represents a object
 * that can handle each, cliend-side, part of the protocol.<br />
 * Each method <tt>sendMessage(Message)</tt> will serialize and send
 * the message to the server.<br />
 * Each method <tt>handleMessage(message)</tt> will handle the received message
 * and eventually will change the client-state or generate the response.<br />
 * @see Message
 */
public interface ServiceClientProtocolHandler {
	
	/**
	 * Serializes and send a {@link ServiceStart} message to the server.
	 * @param startService The message to send.
	 */
	void sendServiceStart(SocketAddress to, ServiceStart startService);
	
	/**
	 * Serializes and send a {@link ServiceStop} message to the server.
	 * @param stopService The message to send.
	 */
	void sendServiceStop(SocketAddress to, ServiceStop stopService);
	
	/**
	 * Serializes and send a {@link ServiceReply} message to the server.
	 * @param reply The message to send.
	 */
	void sendServiceReply(SocketAddress to, ServiceReply reply);
	
	/**
	 * Serializes and send a {@link ServiceDecline} message to the server.
	 * @param decline The message to send.
	 */
	void sendServiceDecline(SocketAddress to, ServiceDecline decline);
	
	/**
	 * Handles a {@link ServiceRequest} message from the server.
	 * @param request The message to handle.
	 */
	void handleServiceRequest(SocketAddress from, ServiceRequest request);
	
	/**
	 * Handles an {@link ServiceOk} message from the server.
	 * @param ok The message to send.
	 */
	void handleServiceOk(SocketAddress from, ServiceOk ok);
}