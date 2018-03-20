package server;

import java.util.function.BiConsumer;
import java.util.logging.Level;

import protocol.AbstractMessageHandler;
import protocol.Decline;
import protocol.Flag;
import protocol.Forget;
import protocol.Init;
import protocol.Message;
import protocol.Reply;
import protocol.StartService;
import protocol.StopService;
import util.Cheat;
import util.Creator;
import util.SerializerBuffer;

public class ServerMessageHandler extends AbstractMessageHandler {

	private final Server server;
	private ClientIdentifier clientId;
	
	public ServerMessageHandler(Server server, SerializerBuffer serializerBuffer, ClientIdentifier clientId) {
		super(serializerBuffer);
		this.server = server;
		this.clientId = clientId;
	}
	
	protected void handleProtocol() {
		synchronized (serializerBuffer) {
			byte flag = serializerBuffer.get();
				
			switch(flag) {
				case Flag.INIT: handleInit(); break;
				case Flag.FORGET: handleForget(clientId); break;
				case Flag.START_SERVICE: handleStartService(clientId); break;
				case Flag.STOP_SERVICE: handleStopService(clientId); break;
				case Flag.REPLY : handleReply(clientId);  break;
				case Flag.DECLINE : handleDecline(clientId);  break;
				default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
			}
		}
	}
	
	private <M extends Message,T> void handleMessage(SerializerBuffer serializerBuffer, T info, Creator<M> messageCreator, BiConsumer<T,M> handler) {
		M message = messageCreator.init();
		message.readFromBuff(serializerBuffer);
		Cheat.LOGGER.log(Level.FINER, message + " received.");
		handler.accept(info, message);
	}
	
	private void handleInit() {
		Init init = Init.CREATOR.init();
		init.readFromBuff(serializerBuffer);
		
		ClientIdentifier clientId = new ClientIdentifier.BUILDER(init.getName())
														.nbTaskMax(init.getNbTaskMax())
														.nbProcessUnits(init.getNbProcessUnits())
														.build();
		
		server.update(this.clientId, clientId);
		server.handleInit(clientId, init);
		this.clientId = clientId;			
	}
	
	private void handleForget(ClientIdentifier clientId) {
		handleMessage(serializerBuffer, clientId, Forget.CREATOR, server::handleForget);
	}
	
	private void handleReply(ClientIdentifier clientId) {
		handleMessage(serializerBuffer, clientId, Reply.CREATOR, server::handleReply);
	}
	
	private void handleStartService(ClientIdentifier clientId) {
		handleMessage(serializerBuffer, clientId, StartService.CREATOR, server::handleStartService);
	}
	
	private void handleStopService(ClientIdentifier clientId) {
		handleMessage(serializerBuffer, clientId, StopService.CREATOR, server::handleStopService);
	}
	
	private void handleDecline(ClientIdentifier clientId) {
		handleMessage(serializerBuffer, clientId, Decline.CREATOR, server::handleDecline);
	}
	
}
