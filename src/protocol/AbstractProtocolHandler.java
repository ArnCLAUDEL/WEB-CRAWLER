package protocol;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.logging.Level;

import util.Cheat;
import util.SerializerBuffer;

public class AbstractProtocolHandler {
	protected final static long DEFAULT_FIRST_TIME_RETRY = 10_000;
	protected final static long DEFAULT_PERIOD_RETRY = 10_000;
	
	protected final SerializerBuffer serializerBuffer;
	
	public AbstractProtocolHandler() {
		this.serializerBuffer = new SerializerBuffer();
	}
	
	protected <M extends Message> TimerTask retry(M message, Consumer<? super M> consumer) {
		return new TimerTask() {
			@Override
			public void run() {
				Cheat.LOGGER.log(Level.FINER, "Re-sending message " + message + "..");
				consumer.accept(message);
			}
		};
	}
	
	protected void schedule(TimerTask task, long firstTime, long period) {
		new Timer().scheduleAtFixedRate(task, firstTime, period);
	}
	
}
