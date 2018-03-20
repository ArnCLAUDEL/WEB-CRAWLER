package protocol;

import java.util.function.Consumer;
import java.util.logging.Level;

import util.Cheat;
import util.SerializerBuffer;

public abstract class AbstractMessageHandler implements Runnable {
	protected final SerializerBuffer serializerBuffer;
	
	private boolean stop;

	public AbstractMessageHandler(SerializerBuffer serializerBuffer) {
		this.serializerBuffer = serializerBuffer;
		this.serializerBuffer.setUnderflowCallback(underflowCallback());
		this.stop = false;
	}
	
	protected Consumer<? super SerializerBuffer> underflowCallback() {
		return (serializerBuffer) -> {
			try {
				synchronized (serializerBuffer) {
					Cheat.LOGGER.log(Level.FINE, "Waiting for re-filling..");
					serializerBuffer.wait();
				}
			} catch (InterruptedException e) {
				shutdown();
			}
		};
	}
	
	public void shutdown() {
		stop = true;
		synchronized (serializerBuffer) {
			serializerBuffer.notifyAll();
		}
	}
	
	protected abstract void handleProtocol();
	
	@Override
	public void run() {
		serializerBuffer.clear();
		serializerBuffer.flip();
		while(!stop) {
			handleProtocol();
		}
	}
	
}
