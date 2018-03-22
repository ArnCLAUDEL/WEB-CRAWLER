package client;

import java.util.function.Consumer;
import java.util.logging.Level;

import protocol.Abort;
import protocol.AbstractMessageHandler;
import protocol.Flag;
import protocol.Message;
import protocol.Ok;
import protocol.Request;
import util.Cheat;
import util.Creator;
import util.SerializerBuffer;

public class ClientMessageHandler extends AbstractMessageHandler {

	private final Client client;
	
	public ClientMessageHandler(SerializerBuffer serializerBuffer, Client client) {
		super(serializerBuffer);
		this.client = client;
	}

	@Override
	protected void handle() {
		synchronized (serializerBuffer) {
			byte flag = serializerBuffer.get();
			
			switch(flag) {
				case Flag.OK: handleOk(); break;
				case Flag.REQUEST: handleRequest(); break;
				case Flag.ABORT: handleAbort(); break;
				default : Cheat.LOGGER.log(Level.WARNING, "Unknown protocol flag : " + flag);
			}
		}
	}
	
	private <M extends Message> void handleMessage(SerializerBuffer serializerBuffer, Creator<M> messageCreator, Consumer<M> handler) {
		M message = messageCreator.init();
		message.readFromBuff(serializerBuffer);
		Cheat.LOGGER.log(Level.FINER, message + " received.");
		handler.accept(message);
		
	}
	
	private void handleOk() {
		handleMessage(serializerBuffer, Ok.CREATOR, client::handleOk);	
	}
	
	private void handleRequest() {
		handleMessage(serializerBuffer, Request.CREATOR, client::handleRequest);
	}
	
	private void handleAbort() {
		handleMessage(serializerBuffer, Abort.CREATOR, client::handleAbort);
	}
}