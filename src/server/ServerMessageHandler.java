package server;

import java.nio.channels.WritableByteChannel;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import io.Creator;
import io.SerializerBuffer;
import protocol.AbstractMessageHandler;
import protocol.ClientIdentifier;
import protocol.Decline;
import protocol.Flag;
import protocol.Forget;
import protocol.Init;
import protocol.Message;
import protocol.Reply;
import protocol.StartService;
import protocol.StopService;
import util.Cheat;

public class ServerMessageHandler extends AbstractMessageHandler {

	private final Server server;
	private final ServerNetworkHandler networkHandler;
	
	private Optional<ClientIdentifier> optionalClientId;
	
	public ServerMessageHandler(Server server, SerializerBuffer serializerBuffer, ServerNetworkHandler networkHandler, WritableByteChannel channel) {
		super(serializerBuffer, channel);
		this.server = server;
		this.networkHandler = networkHandler;
		this.optionalClientId = Optional.empty();
	}
	
	protected void handleProtocol() {
		synchronized (serializerBuffer) {
			byte flag = serializerBuffer.get();
			if(flag == Flag.INIT) {
				handleInit();
				return;
			}	
			
			if(!optionalClientId.isPresent()) {
				Cheat.LOGGER.log(Level.WARNING, "Missing ClientIdentifier.");
				return;
			}
			
			ClientIdentifier clientId = optionalClientId.get();
			
			switch(flag) {
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
		
		ClientIdentifier clientId = new ClientIdentifier.BUILDER(init.getName(), channel)
														.nbTaskMax(init.getNbTaskMax())
														.nbProcessUnits(init.getNbProcessUnits())
														.build();
		
		if(server.handleInit(clientId, init)) {
			this.optionalClientId = Optional.of(clientId);
			this.networkHandler.addClient(channel, clientId);
		}
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
