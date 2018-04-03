package service.server;

import io.IOEntity;
import protocol.Message;
import service.message.Reply;
import service.message.Request;

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
 * @see ServerProtocolHandler
 * @see NetworkWriter
 * @see ClientIdentifier
 * @see Request
 * @see Reply
 */
public interface Server extends IOEntity, ServerProtocolHandler, NetworkWriter {	
	
	/**
	 * Adds a client to the server as <tt>inactive</tt> client.<br />
	 * Inactive clients can communicate with the server but no {@link Request} will be sent to them.
	 * @param clientId The client to add
	 * @return <tt>true</tt> if the client has been added
	 */
	boolean addClient(ClientIdentifier clientId);
	
	/**
	 * Removes a client from the server.<br />
	 * The client will also be removed from <tt>active</tt> clients if necessary.
	 * @param clientId The client to remove
	 */
	void removeClient(ClientIdentifier clientId);
	
	/**
	 * Sets the client as <tt>active</tt> or <tt>inactive</tt>.<br />
	 * {@link Request} are only sent to active clients.
	 * @param clientId The client to update
	 * @param active <tt>true</tt> if the client should be set as active
	 */
	void setClientActivity(ClientIdentifier clientId, boolean active);
	
	/**
	 * Scans a web site with the given hostname.<br /> 
	 * Requests will be automatically sent to <tt>active</tt> clients and future replies will be handled and processed by
	 * the method <tt>processReply(Reply)</tt>.
	 * @param hostname The hostname of the web site to scan.
	 */
	void scan(String hostname);
	
	/**
	 * Sends the given {@link Request} to <tt>active</tt> clients.<br />
	 * @param request The request to send
	 * @return <tt>true</tt> if the request has been sent too at least one client
	 */
	boolean sendRequest(Request request);
	
	/**
	 * Handles and starts the processing of a {@link Reply}.<br />
	 * More {@link Request} can eventually be generated and sent.
	 * @param reply The reply to process.
	 */
	void processReply(Reply reply);
	
	/**
	 * Updates the {@link ClientIdentifier} of a client.<br />
	 * This could be used to complete the registration of a new client,
	 * or update informations of an existing one.
	 * @param oldClientId The old client idenfifier
	 * @param newClientId The new client idenfitier
	 */
	void update(ClientIdentifier oldClientId, ClientIdentifier newClientId);
}
