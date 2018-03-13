package client.state;

import java.nio.channels.SocketChannel;
import java.util.logging.Level;

import client.Client;
import protocol.Decline;
import protocol.Reply;
import protocol.Request;
import protocol.StopService;
import util.Cheat;

public class ActiveClientProtocolHandler extends AbstractClientProtocolHandler {
	
	public ActiveClientProtocolHandler(Client client, SocketChannel channel) {
		super(client, channel);
	}
	
	@Override
	public void sendReply(Reply reply) {
		// TODO
		serializerBuffer.clear();
		reply.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send())
			Cheat.LOGGER.log(Level.INFO, "Reply sent.");
	}
	
	@Override
	public void sendDecline(Request request) {
		// TODO
		serializerBuffer.clear();
		Decline decline = new Decline();
		decline.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send())
			Cheat.LOGGER.log(Level.INFO, "Decline sent.");
	}
	
	@Override
	public void sendStopService() {
		// TODO
		serializerBuffer.clear();
		StopService stopService = new StopService();
		stopService.writeToBuff(serializerBuffer);
		serializerBuffer.flip();
		if(send())
			Cheat.LOGGER.log(Level.INFO, "Reply sent.");
	}
	
	@Override
	public void handleRequest(Request request) {
		// TODO 
		sendReply(new Reply());
		Cheat.LOGGER.log(Level.INFO, "Server Request handled. (not yet implemented)");		
	}
	
	@Override
	public void handleAbort(Request request) {
		// TODO 
		Cheat.LOGGER.log(Level.INFO, "Server Abort handled. (not yet implemented)");
	}
	
	@Override
	public String toString() {
		return "Active";
	}
	
}
