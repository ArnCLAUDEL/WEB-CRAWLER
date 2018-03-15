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
import protocol.Decline;
import protocol.Flag;
import protocol.Forget;
import protocol.Init;
import protocol.Reply;
import protocol.StartService;
import protocol.StopService;
import util.Cheat;

public class ServerNetworkHandler extends AbstractNetworkHandler {

	private final Map<SocketChannel, ClientIdentifier> clients;
	private final Server server;
	
	public ServerNetworkHandler(ServerSocketChannel channel, Server server) throws IOException {
		super(channel, SelectionKey.OP_ACCEPT, server);
		this.server = server;
		this.clients = new HashMap<>();
	}
	
	private void remove(SocketChannel channel) {
		ClientIdentifier clientId = this.clients.remove(channel);
		server.removeClient(clientId);
	}
	
	@Override
	public void handleAcceptOperation(SelectionKey sk) {
		try {
			SocketChannel socket = ((ServerSocketChannel) sk.channel()).accept();
			addChannel(socket, SelectionKey.OP_READ);
			getSerializerBuffer(socket).setOverflowCallback(serializerBufferFiller(socket));
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
				remove(sc);
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
			case Flag.FORGET: handleForget(clientId, serializerBuffer); break;
			case Flag.START_SERVICE: handleStartService(clientId, serializerBuffer); break;
			case Flag.STOP_SERVICE: handleStopService(clientId, serializerBuffer); break;
			case Flag.REPLY : handleReply(clientId, serializerBuffer);  break;
			case Flag.DECLINE : handleDecline(clientId, serializerBuffer);  break;
			default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
		}		
	}
	
	private void handleInit(SocketChannel channel, SerializerBuffer serializerBuffer) {
		Init init = Init.CREATOR.init();
		init.readFromBuff(serializerBuffer);
		
		ClientIdentifier clientId = new ClientIdentifier.BUILDER(init.getName(), channel)
														.nbTaskMax(init.getNbTaskMax())
														.nbProcessUnits(init.getNbProcessUnits())
														.build();
		
		if(server.handleInit(clientId, init))
			this.clients.put(channel, clientId);
	}
	
	private void handleForget(ClientIdentifier clientId, SerializerBuffer serializerBuffer) {
		handleMessage(serializerBuffer, clientId, Forget.CREATOR, server::handleForget);
	}
	
	private void handleReply(ClientIdentifier clientId, SerializerBuffer serializerBuffer ) {
		handleMessage(serializerBuffer, clientId, Reply.CREATOR, server::handleReply);
	}
	
	private void handleStartService(ClientIdentifier clientId, SerializerBuffer serializerBuffer ) {
		handleMessage(serializerBuffer, clientId, StartService.CREATOR, server::handleStartService);
	}
	
	private void handleStopService(ClientIdentifier clientId, SerializerBuffer serializerBuffer ) {
		handleMessage(serializerBuffer, clientId, StopService.CREATOR, server::handleStopService);
	}
	
	private void handleDecline(ClientIdentifier clientId, SerializerBuffer serializerBuffer ) {
		handleMessage(serializerBuffer, clientId, Decline.CREATOR, server::handleDecline);
	}

}
