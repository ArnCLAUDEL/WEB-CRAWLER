package server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractUDPNetworkHandler;
import util.BiMap;
import util.Cheat;
import util.SerializerBuffer;

public class ServerUDPNetworkHandler extends AbstractUDPNetworkHandler implements ServerNetworkHandler {
	private final ExecutorService executor;
	private final Map<SocketAddress, ServerMessageHandler> clientsMessageHandler;
	private final BiMap<ClientIdentifier, SocketAddress> clientsAddresses;
	private final Server server;
	
	public ServerUDPNetworkHandler(DatagramChannel channel, int op, Server server) throws IOException {
		super(channel, SelectionKey.OP_READ, server);
		this.server = server;
		this.executor = Executors.newCachedThreadPool();
		this.clientsMessageHandler = new HashMap<>();
		this.clientsAddresses = new BiMap<>();
	}
	
	@Override
	public void update(ClientIdentifier oldClientId, ClientIdentifier newClientId) {
		SocketAddress address = clientsAddresses.removeLeft(oldClientId);
		if(address != null) {
			clientsAddresses.put(newClientId, address);
		}
	}
	
	private void remove(SocketAddress address) {
		ServerMessageHandler messageHandler = this.clientsMessageHandler.remove(address);
		messageHandler.shutdown();
		ClientIdentifier clientId = clientsAddresses.removeRight(address);
		server.setClientActivity(clientId, false);
	}
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		Cheat.LOGGER.log(Level.INFO, "Datagram channel disconnected.");
	}

	@Override
	public int write(ClientIdentifier clientId, SerializerBuffer serializerBuffer) throws IOException {
		SocketAddress address = clientsAddresses.getLeft(clientId);
		if(address == null)
			return -1;
		
		return send(address, serializerBuffer);
	}
	
	@Override
	protected void register(SocketAddress address, SerializerBuffer serializerBuffer) {
		ClientIdentifier clientId = ClientIdentifier.makeUnregistered();
		ServerMessageHandler messageHandler = new ServerMessageHandler(server, serializerBuffer, clientId);
		executor.execute(messageHandler);
		clientsMessageHandler.put(address, messageHandler);
		clientsAddresses.put(clientId, address);
	}
	
}
