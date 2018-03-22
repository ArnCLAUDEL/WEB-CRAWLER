package protocol;

import java.util.function.Consumer;
import java.util.logging.Level;

import io.AbstractHandler;
import util.Cheat;
import util.SerializerBuffer;

public abstract class AbstractMessageHandler extends AbstractHandler {
	protected final SerializerBuffer serializerBuffer;
	
	private boolean stop;

	public AbstractMessageHandler(SerializerBuffer serializerBuffer) {
		super();
		this.serializerBuffer = serializerBuffer;
		this.serializerBuffer.clear();
		this.serializerBuffer.flip();
		this.serializerBuffer.setUnderflowCallback(underflowCallback());
		this.stop = false;
	}
	
	protected Consumer<? super SerializerBuffer> underflowCallback() {
		return (serializerBuffer) -> {
			try {
				synchronized (serializerBuffer) {
					Cheat.LOGGER.log(Level.FINEST, "Waiting for re-filling..");
					serializerBuffer.wait();
				}
			} catch (InterruptedException e) {
				shutdown();
			}
		};
	}
	
	@Override
	public void shutdown() {
		stop = true;
		synchronized (serializerBuffer) {
			serializerBuffer.notifyAll();
		}
	}
	
	@Override
	protected boolean stop() {
		return stop;
	}	
	
}
