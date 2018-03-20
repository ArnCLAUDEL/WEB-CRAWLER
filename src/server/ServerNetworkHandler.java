package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractTCPNetworkHandler;
import util.Cheat;
import util.SerializerBuffer;

public class ServerNetworkHandler extends AbstractTCPNetworkHandler implements NetworkWriter {
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<SocketChannel, ServerMessageHandler> clientsMessageHandler;
	private final Map<ClientIdentifier, SocketChannel> clientsChannel;
	private final Map<SocketChannel, ClientIdentifier> channelsClient;
	private final Server server;
	
	public ServerNetworkHandler(ServerSocketChannel channel, Server server) throws IOException {
		super(channel, SelectionKey.OP_ACCEPT, server);
		this.server = server;
		this.clientsMessageHandler = new HashMap<>();
		this.clientsChannel = new HashMap<>();
		this.channelsClient = new HashMap<>();
	}
	
	public void update(ClientIdentifier oldClientId, ClientIdentifier newClientId) {
		SocketChannel channel = clientsChannel.remove(oldClientId);
		if(channel != null) {
			clientsChannel.put(newClientId, channel);
			channelsClient.put(channel, newClientId);
		}
	}
	
	private void remove(SocketChannel channel) {
		ServerMessageHandler messageHandler = this.clientsMessageHandler.remove(channel);
		messageHandler.shutdown();
		ClientIdentifier clientId = channelsClient.remove(channel);
		clientsChannel.remove(clientId);
		server.setClientActivity(clientId, false);
	}
	
	@Override
	public int write(ClientIdentifier clientId, SerializerBuffer serializerBuffer) throws IOException {
		SocketChannel channel = clientsChannel.get(clientId);
		if(channel == null)
			return -1;
		return write(channel, serializerBuffer);
	}
	
	@Override
	protected void register(SocketChannel channel, SerializerBuffer buffer) {
		ClientIdentifier clientId = ClientIdentifier.makeUnregistered();
		ServerMessageHandler messageHandler = new ServerMessageHandler(server, buffer, clientId);
		executor.execute(messageHandler);
		clientsMessageHandler.put(channel, messageHandler);
		clientsChannel.put(clientId, channel);
		channelsClient.put(channel, clientId);
	}
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		remove((SocketChannel) sk.channel());
		Cheat.LOGGER.log(Level.INFO, "Client disconnected.");
	}
}
