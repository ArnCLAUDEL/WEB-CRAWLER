package protocol;

import java.nio.BufferUnderflowException;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.AbstractHandler;
import util.Cheat;
import util.SerializerBuffer;

/**
 * {@code AbstractMessageHandler} provides a base implementation for a
 * message handler.<br />
 * This works with an existing {@link SerializerBuffer} and it will 
 * simply add to it an {@link BufferUnderflowException} callback.
 * Hence when it will try to read, from the buffer, some data receive from the network,
 * and a {@code BufferUnderflowException} occurs, it will wait on the buffer.
 * @see AbstractHandler
 * @see SerializerBuffer
 * @see BufferUnderflowException
 */
public abstract class AbstractMessageHandler extends AbstractHandler {
	
	/**
	 * The buffer to read from.
	 */
	protected final SerializerBuffer serializerBuffer;
	
	/**
	 * Indicates if the handler should stop or not.
	 */
	private boolean stop;

	/**
	 * Creates a new instance with the given buffer.<br />
	 * The buffer's methods <tt>clear()</tt> and <tt>flip()</tt> are called.
	 * An {@link BufferUnderflowException} callback is also set.
	 * @param serializerBuffer The buffer to read the data from.
	 */
	public AbstractMessageHandler(SerializerBuffer serializerBuffer) {
		super();
		this.serializerBuffer = serializerBuffer;
		this.serializerBuffer.clear();
		this.serializerBuffer.flip();
		this.serializerBuffer.setUnderflowCallback(underflowCallback());
		this.stop = false;
	}
	
	/**
	 * Returns an {@link BufferUnderflowException} callback that will wait
	 * on the buffer and stops this handler if interrupted.
	 * @return The {@code BufferUnderflowException} callback.
	 */
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
	
	/**
	 * Stops the handler and notify everyone waiting on the buffer.
	 */
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
