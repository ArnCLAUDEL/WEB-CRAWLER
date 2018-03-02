package process;

import java.util.concurrent.Callable;

public class ProcessTask<V> extends ProcessFuture<V> implements Runnable {

	private Callable<V> callable;
	
	public ProcessTask(Callable<V> callable) {
		this.callable = callable;
	}
	
	@Override
	public void run() {
		try {
			V result = callable.call();
			setResult(result);
		} catch (Exception e) {
			setException(e);
		} finally {
			done = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}
}