package app.service.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractTCPNetworkHandler;
import io.Handler;
import protocol.NetworkWriter;
import session.SessionInfo;
import util.BiMap;
import util.Cheat;
import util.SerializerBuffer;

public class ExplorerServerTCPNetworkHandler extends AbstractTCPNetworkHandler implements NetworkWriter {

	private final Executor executor;
	private final ExplorerServer server;
	private final BiMap<SocketChannel, SocketAddress> channelsAddress;
	private final Map<SocketAddress, SocketAddress> sessionAddressToRealAddress;
	private final Map<SocketChannel, ExplorerServerTCPMessageHandler> handlers;
	
	public ExplorerServerTCPNetworkHandler(ServerSocketChannel channel, ExplorerServer server) throws IOException {
		super(channel, SelectionKey.OP_ACCEPT, server);
		this.server = server;
		this.executor = Executors.newCachedThreadPool();
		this.handlers = new HashMap<>();
		this.sessionAddressToRealAddress = new HashMap<>();
		this.channelsAddress = new BiMap<>();
	}
	
	@Override
	protected void register(SocketChannel channel, SerializerBuffer buffer) {
		super.register(channel, buffer);
		try {
			InetSocketAddress address = (InetSocketAddress) channel.getRemoteAddress();
			SessionInfo info = server.getSession(address);
			sessionAddressToRealAddress.put(info.getCertificate().getAddress(), channel.getRemoteAddress());
			channelsAddress.put(channel, address);
			ExplorerServerTCPMessageHandler handler = new ExplorerServerTCPMessageHandler(buffer, info, server);
			handlers.put(channel, handler);
			executor.execute(handler);
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Failed to register the SocketChannel", e);
		} catch (NoSuchElementException e) {
			Cheat.LOGGER.log(Level.WARNING, "Session not found", e);
			try {channel.close();} 
			catch (IOException e1) {}
		}
	}
	
	@Override
	protected void channelClosedCallback(SelectionKey sk) throws IOException {
		super.channelClosedCallback(sk);
		Handler handler = handlers.remove(sk.channel());
		if(handler != null)
			handler.shutdown();
	}

	@Override
	public int write(SocketAddress address, SerializerBuffer buffer) throws IOException {
		SocketChannel channel = channelsAddress.getRight(sessionAddressToRealAddress.get(address));
		if(channel == null)
			throw new IOException("SocketChannel not available");
		return write(channel, buffer);
	}

}
