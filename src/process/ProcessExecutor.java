package process;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.logging.Level;

import util.Cheat;

public class ProcessExecutor implements Executor {
	public final static int THREAD_CAPACITY = Runtime.getRuntime().availableProcessors();
	public final static int TASK_CAPACITY = 100;
		
	private final List<Thread> threads = new ArrayList<>(THREAD_CAPACITY);
	private final List<TaskExecutor> taskExecutors = new ArrayList<>(THREAD_CAPACITY);
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(TASK_CAPACITY);
	
	public ProcessExecutor(int nbThreadInit) {
		if(!checkNbInit(nbThreadInit))
			throw new IllegalArgumentException("Incorrect number of thread to initialize");
		
		TaskExecutor task;
		Thread thread;
		for(int i = 0; i < nbThreadInit; i ++) {
			 task = new TaskExecutor(queue);
			 thread = new Thread(task);
			 threads.add(thread);
			 taskExecutors.add(task);
			 thread.start();
		}
	}
	
	public ProcessExecutor() {
		this(THREAD_CAPACITY);
	}
	
	private boolean checkNbInit(int nb) {
		return nb >= 0 && nb <= THREAD_CAPACITY;
	}
	
	public static void main(String[] args) {
		Cheat.setLoggerLevelDisplay(Level.FINE);
		ProcessExecutor executor = new ProcessExecutor();
		
		List<Future<Set<String>>> futureResults = new ArrayList<>();
		
		Future<Set<String>> futureResult = executor.submit(new ProcessUnit("https://en.wiktionary.org/wiki/Captain_Obvious"));
		for(int i = 0; i < 200; i++) {
			try {Thread.sleep(100);}
			catch(InterruptedException e) {}
			futureResult = executor.submit(new ProcessUnit("https://en.wiktionary.org/wiki/"+i));
			futureResults.add(futureResult);
		}
		
		synchronized (executor.queue) {
			while(!executor.queue.isEmpty()) {
				try {executor.queue.wait();}
				catch (InterruptedException e) {}
			}
		}
		
		executor.taskExecutors.stream().forEach(t -> t.stop(true));
		futureResults.stream().forEach(f -> {
			try {
				f.get();
			} catch (ExecutionException | InterruptedException e) {
				Cheat.LOGGER.log(Level.WARNING, e.getMessage());
			}
		});
	}
	
	@Override
	public void execute(Runnable runnable) {
		boolean taskSubmitted = false;
		while(!taskSubmitted) {
			try {
				synchronized (queue) {
					queue.put(runnable);
					queue.notifyAll();
				}
				taskSubmitted = true;
			} catch (InterruptedException e) {
				Cheat.LOGGER.log(Level.WARNING, "Interruption during task submission", e);
			}
		}
	}
	
	public <T> Future<T> submit(Callable<T> callable) {
		ProcessTask<T> task = new ProcessTask<T>(callable);
		execute(task);
		Cheat.LOGGER.log(Level.FINER, "New task submitted");
		return task;
	}
	
	public Future<Set<String>> scan(String hostname) {
		return submit(new ProcessUnit(hostname));
	}
	
}