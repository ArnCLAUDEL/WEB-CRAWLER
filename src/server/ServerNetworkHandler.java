package server;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import io.AbstractNetworkHandler;
import io.SerializerBuffer;
import protocol.ClientIdentifier;
import protocol.Decline;
import protocol.Flag;
import protocol.Forget;
import protocol.Init;
import protocol.Reply;
import protocol.StartService;
import protocol.StopService;
import util.Cheat;

public class ServerNetworkHandler extends AbstractNetworkHandler {

	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<WritableByteChannel, ClientIdentifier> clientsId;
	private final Map<ReadableByteChannel, ServerMessageHandler> clientsMessageHandler;
	private final Server server;
	
	public ServerNetworkHandler(ServerSocketChannel channel, Server server) throws IOException {
		super(channel, SelectionKey.OP_ACCEPT, server);
		this.server = server;
		this.clientsId = new HashMap<>();
		this.clientsMessageHandler = new HashMap<>();
	}
	
	public void addClient(WritableByteChannel channel, ClientIdentifier clientId) {
		this.clientsId.put(channel, clientId);
	}
	
	private void remove(SocketChannel channel) {
		ServerMessageHandler messageHandler = this.clientsMessageHandler.remove(channel);
		ClientIdentifier clientId = this.clientsId.remove(channel);
		messageHandler.shutdown();
		server.removeClient(clientId);
	}
	
	@Override
	public void handleAcceptOperation(SelectionKey sk) {
		try {
			SocketChannel socket = ((ServerSocketChannel) sk.channel()).accept();
			addChannel(socket, SelectionKey.OP_READ);
			SerializerBuffer serializerBuffer = getSerializerBuffer(socket);
			ServerMessageHandler messageHandler = new ServerMessageHandler(server, serializerBuffer, this, socket);
			executor.execute(messageHandler);
			clientsMessageHandler.put(socket, messageHandler);
			Cheat.LOGGER.log(Level.INFO,"Connection from " + socket.getRemoteAddress() + " accepted");
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	@Override
	public void handleReadOperation(SelectionKey sk, SerializerBuffer serializerBuffer) {
		Cheat.LOGGER.log(Level.FINER, "Message received.");
		
		SocketChannel sc = (SocketChannel) sk.channel();
		try {
			System.out.println("Before buffer");
			synchronized (serializerBuffer) {
				System.out.println("Entering buffer");
				serializerBuffer.compact();
				int read = serializerBuffer.read(sc);
				System.out.println(read + " bytes read from the socket");
				if(read < 0) {
					remove(sc);
					sk.cancel();
					sc.close();
					Cheat.LOGGER.log(Level.INFO, "Client disconnected.");
					return;
				}
				serializerBuffer.flip();
				serializerBuffer.notifyAll();
				System.out.println("Leaving buffer");
			}
			System.out.println("After buffer");
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, "Error while filling buffer.", e);
		}
	}

}
