package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import io.AbstractNetworkHandler;
import io.SerializerBuffer;
import protocol.ClientIdentifier;
import protocol.Flag;
import protocol.Init;
import protocol.Reply;
import util.Cheat;

public class ServerNetworkHandler extends AbstractNetworkHandler {

	private final Map<SocketChannel, ClientIdentifier> clients;
	private final Server server;
	
	public ServerNetworkHandler(ServerSocketChannel channel, Server server) throws IOException {
		super(channel, SelectionKey.OP_ACCEPT, server);
		this.server = server;
		this.clients = new HashMap<>();
	}
	
	@Override
	public void handleAcceptOperation(SelectionKey sk) {
		try {
			SocketChannel socket = ((ServerSocketChannel) sk.channel()).accept();
			addChannel(socket, SelectionKey.OP_READ);
			Cheat.LOGGER.log(Level.INFO,"Connection from " + socket.getRemoteAddress() + " accepted");
		} catch (IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	@Override
	public void handleReadOperation(SelectionKey sk, SerializerBuffer serializerBuffer) {
		Cheat.LOGGER.log(Level.FINER, "Message received.");
		
		serializerBuffer.clear();
		SocketChannel sc = (SocketChannel) sk.channel();
		try {
			if(serializerBuffer.read(sc) < 0) {
				sk.cancel();
				sc.close();
				Cheat.LOGGER.log(Level.INFO, "Client disconnected.");
				return;
			}
			serializerBuffer.flip();
			handleProtocol(sc, serializerBuffer);
		} catch(IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	protected void handleProtocol(SocketChannel channel, SerializerBuffer serializerBuffer) {
		// TODO
		byte flag = serializerBuffer.get();
		if(flag == Flag.INIT) {
			handleInit(channel, serializerBuffer);
			return;
		}
		ClientIdentifier clientId = clients.get(channel);		
		
		switch(flag) {
			case Flag.FORGET: server.handleForget(clientId); break;
			case Flag.START_SERVICE: server.handleStartService(clientId); break;
			case Flag.STOP_SERVICE: server.handleStopService(clientId); break;
			case Flag.REPLY : handleReply(clients.get(channel));  break;
			default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
		}		
	}
	
	
	private void handleInit(SocketChannel channel, SerializerBuffer serializerBuffer) {
		Init initMessage = Init.CREATOR.init();
		initMessage.readFromBuff(serializerBuffer);
		
		ClientIdentifier clientId = new ClientIdentifier.BUILDER(initMessage.getName(), channel)
														.nbTaskMax(initMessage.getNbTaskMax())
														.nbProcessUnits(initMessage.getNbProcessUnits())
														.build();
		
		if(server.handleInit(clientId))
			this.clients.put(channel, clientId);
	}
	
	private void handleReply(ClientIdentifier clientId) {
		// TODO
		/* 	Retrieve data
		 	Build a Reply
		 */
		server.handleReply(clientId, new Reply());
	}

}
