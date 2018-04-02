package server.impl;

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
import server.ClientIdentifier;
import server.Server;
import server.ServerNetworkHandler;
import util.BiMap;
import util.Cheat;
import util.SerializerBuffer;

public class ServerTCPNetworkHandler extends AbstractTCPNetworkHandler implements ServerNetworkHandler {
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<SocketChannel, ServerMessageHandler> clientsMessageHandler;
	private final BiMap<ClientIdentifier, SocketChannel> clientsChannels;
	private final Server server;
	
	public ServerTCPNetworkHandler(ServerSocketChannel channel, Server server) throws IOException {
		super(channel, SelectionKey.OP_ACCEPT, server);
		this.server = server;
		this.clientsMessageHandler = new HashMap<>();
		this.clientsChannels = new BiMap<>();
	}
	
	@Override
	public void update(ClientIdentifier oldClientId, ClientIdentifier newClientId) {
		SocketChannel channel = clientsChannels.removeLeft(oldClientId);
		if(channel != null) {
			clientsChannels.put(newClientId, channel);
		}
	}
	
	private void remove(SocketChannel channel) {
		ServerMessageHandler messageHandler = this.clientsMessageHandler.remove(channel);
		messageHandler.shutdown();
		ClientIdentifier clientId = clientsChannels.removeRight(channel);
		clientsChannels.removeLeft(clientId);
		server.setClientActivity(clientId, false);
	}
	
	@Override
	public int write(ClientIdentifier clientId, SerializerBuffer serializerBuffer) throws IOException {
		SocketChannel channel = clientsChannels.getLeft(clientId);
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
		clientsChannels.put(clientId, channel);
	}
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		remove((SocketChannel) sk.channel());
		Cheat.LOGGER.log(Level.INFO, "Client disconnected.");
	}
}
