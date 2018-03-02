package process;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;

import util.Cheat;

public class TaskExecutor implements Runnable {
	private final static long TIME_TO_WAIT = 1000;
	
	private final BlockingQueue<Runnable> queue; 
	
	private Optional<Runnable> currentTask;
	private boolean stop;
	
	public TaskExecutor(BlockingQueue<Runnable> queue) {
		this.queue = queue;
		this.currentTask = Optional.empty();
		this.stop = false;
	}
	
	public void stop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		Cheat.LOGGER.log(Level.INFO, "Task Executor " + Thread.currentThread().getId() + " is starting.");
		while(!stop) {
			try {
				synchronized (queue){
					while(queue.isEmpty() && !stop)
						queue.wait(TIME_TO_WAIT);
					currentTask = Optional.ofNullable(queue.poll());
					queue.notifyAll();
				}
				Runnable r = currentTask.orElseGet(() -> () -> {});
				r.run();
				currentTask = Optional.empty();
			} catch (InterruptedException e) {}
		}
		Cheat.LOGGER.log(Level.INFO, "Task Executor " + Thread.currentThread().getId() + " is shutting down.");
	}
	
}
