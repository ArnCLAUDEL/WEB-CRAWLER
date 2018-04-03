package service.client.impl;

import java.util.function.Consumer;
import java.util.logging.Level;

import protocol.AbstractMessageHandler;
import protocol.Flag;
import protocol.Message;
import service.client.ClientProtocolHandler;
import service.message.Abort;
import service.message.Ok;
import service.message.Request;
import util.Cheat;
import util.Creator;
import util.SerializerBuffer;

public class ClientMessageHandler extends AbstractMessageHandler {

	private final ClientProtocolHandler protocolHandler;
	
	public ClientMessageHandler(SerializerBuffer serializerBuffer, ClientProtocolHandler protocolHandler) {
		super(serializerBuffer);
		this.protocolHandler = protocolHandler;
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
		handleMessage(serializerBuffer, Ok.CREATOR, protocolHandler::handleOk);	
	}
	
	private void handleRequest() {
		handleMessage(serializerBuffer, Request.CREATOR, protocolHandler::handleRequest);
	}
	
	private void handleAbort() {
		handleMessage(serializerBuffer, Abort.CREATOR, protocolHandler::handleAbort);
	}
}
