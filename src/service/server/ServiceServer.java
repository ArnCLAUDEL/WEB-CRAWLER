package service.server;

import io.IOEntity;
import protocol.Message;
import protocol.NetworkWriter;
import service.message.ServiceReply;
import service.message.ServiceRequest;
import session.SessionInfo;

/**
 * {@code Server} interface represents a server that is able to handle protocol
 * {@link Message} from the network and generate responses.<br />
 * Any outgoing response should be sent using the {@link NetworkWriter} features.<br />
 * Clients are registered and identified with a unique {@link ClientIdentifier}. 
 * A client can be added or removed to the server and his {@code ClientIdentifier} / activity status can
 * be updated at any time.<br />
 * The server is capable to scan an entire web site, using the method <tt>scan(link)</tt>.
 * Requests will be automatically sent to clients and future replies will be handled and processed by
 * the method <tt>processReply(Reply)</tt>.
 * @see IOEntity
 * @see ServiceServerProtocolHandler
 * @see NetworkWriter
 * @see ClientIdentifier
 * @see ServiceRequest
 * @see ServiceReply
 */
public interface ServiceServer extends ServiceServerProtocolHandler{	
	
	void scan(String hostname);
	
	/**
	 * Sends the given {@link ServiceRequest} to <tt>active</tt> clients.<br />
	 * @param request The request to send
	 * @return <tt>true</tt> if the request has been sent too at least one client
	 */
	boolean sendRequest(ServiceRequest request);
	
	/**
	 * Handles and starts the processing of a {@link ServiceReply}.<br />
	 * More {@link ServiceRequest} can eventually be generated and sent.
	 * @param reply The reply to process.
	 */
	void processReply(ServiceReply reply);
	
	/**
	 * Sets the new protocol handler.<br />
	 * This setter can be used with a Design Pattern <tt>State</tt>. 
	 * @param protocolHandler The new protocol handler.
	 */
	void setProtocolHandler(ServiceServerProtocolHandler protocolHandler);
	
	void setClientActivity(SessionInfo info, boolean active);
}
