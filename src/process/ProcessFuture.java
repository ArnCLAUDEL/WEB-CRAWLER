package process;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessFuture<V> implements Future<V>{
	private final static int TIME_TO_WAIT = 1000;
	
	private Exception exception = null;
	private boolean exceptionToThrow = false;
	
	private V v = null;
	protected boolean done = false;
	
	public void setException(Exception exception) {
		this.exception = exception;
		this.exceptionToThrow = true;
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO 
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO 
		return false;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	public void setResult(V v) {
		this.v = v;
	}
	
	@Override
	public V get() throws InterruptedException, ExecutionException {
		while(!done) {
			synchronized (this) {
				wait(TIME_TO_WAIT);
			}
		}
		
		if(exceptionToThrow) {
			throw new ExecutionException(exception);
		}
		
		return v;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		long start = System.nanoTime(), duration = unit.toNanos(timeout), deadline = start + duration;
		long timeToWait;
		boolean expired = duration <= 0;
		System.err.println(start + " | " + duration + " | " + deadline + " | ");
		while(!done && !expired) {
			synchronized (this) {
				if((timeToWait = (deadline - System.nanoTime())) > 0)
					wait(TimeUnit.NANOSECONDS.toMillis(timeToWait));
				System.err.println(timeToWait);
			}
			expired = System.nanoTime() > deadline;
			System.err.println(System.nanoTime() + " > " + deadline + " -> " + expired);
		}
		
		if(expired)
			throw new TimeoutException("Timer expired");
		
		if(done)
			return get();
		
		return null;
	}
}