package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import io.AbstractNetworkHandler;
import protocol.ClientIdentifier;
import protocol.Flag;
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
	public void handleReadOperation(SelectionKey sk) {
		Cheat.LOGGER.log(Level.FINER, "Message received.");
		buffer.clear();
		SocketChannel sc = (SocketChannel) sk.channel();
		try {
			if(sc.read(buffer) < 0) {
				sk.cancel();
				sc.close();
				Cheat.LOGGER.log(Level.INFO, "Client disconnected.");
				return;
			}
			buffer.flip();
			handleProtocol(sc);
			sc.write(buffer.slice());
		} catch(IOException e) {
			Cheat.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		String message = Cheat.CHARSET.decode(buffer).toString();
		System.out.println(message);
	}
	
	private void handleProtocol(SocketChannel channel) {
		// TODO
		byte flag = buffer.get();
		if(flag == Flag.INIT) {
			handleInit(channel);
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
	
	
	private void handleInit(SocketChannel channel) {
		// TODO 
		/* 	Retrieve data
		 	Build a ClientIdentifier
		*/
		ClientIdentifier clientId = new ClientIdentifier();
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
