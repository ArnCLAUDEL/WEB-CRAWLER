package service.client;

import java.io.IOException;

import io.IOEntity;
import protocol.Message;

/**
 * {@code Client} interface represents a client that is able to handle protocol
 * {@link Message} from the network and generate responses.<br />
 * The connection to the server can be done using the method <tt>connect(hostname,port)</tt>.
 * The behaviour of the client in the protocol, could be implemented by another
 * class that implements {@link ClientProtocolHandler}. The implementing object
 * can be set using the method <tt>setProtocolHandler(ClientProtocolHandler)</tt>.<br />
 * Any outgoing response should be sent using the {@link NetworkWriter} features.<br />
 * The client is also capable to scan a web page with the method <tt>scan(link)</tt>,
 * and retrieve various informations such as other link, frequent words or images.
 * @see IOEntity
 * @see ClientProtocolHandler
 * @see NetworkWriter
 * @see Message
 */
public interface Client extends IOEntity, ClientProtocolHandler, NetworkWriter {
	
	/**
	 * Connects to a server with the given hostname and port.
	 * @param hostname The hostname of the server.
	 * @param port The port of the server.
	 * @throws IOException If an I/O error occurs.
	 */
	void connect(String hostname, int port) throws IOException;
	
	/**
	 * Scans the web page with the given link.<br />
	 * Retrieves various informations such as other links, frequent words or pictures.
	 * @param link The link of the web page to scan.
	 */
	void scan (String hostname, String link);
	
	/**
	 * Sets the new protocol handler.<br />
	 * This setter can be used with a Design Pattern <tt>State</tt>. 
	 * @param protocolHandler The new protocol handler.
	 */
	void setProtocolHandler(ClientProtocolHandler protocolHandler);
	
	/**
	 * Returns the number of process units available for computing.<br />
	 * @return The number of process units available.
	 */
	int getNbProcessUnits();
	
	/**
	 * Returns the max number of task that can be handled at the same time.<br />
	 * When the maximum is reaches, any new task can be silently discarded.
	 * @return The max number of task that can be handled at the same time.
	 */
	int getNbTaskMax();
	
	/**
	 * Sets the id of the client.<br />
	 * The id can be used to identify the client to the server and therefore ease
	 * a reconnection or an activity recovery.
	 * @param id The id of the client.
	 */
	void setId(long id);
	
	/**
	 * Gets the id of the client.<br />
	 * The id can be used to identify the client to the server and therefore ease
	 * a reconnection or an activity recovery.
	 */
	long getId();
}