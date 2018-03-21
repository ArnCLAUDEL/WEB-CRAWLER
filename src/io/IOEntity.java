package io;

public interface IOEntity extends Runnable {
	boolean isActive();
	void shutdown();
	void addHandler(Handler handler);
	void removeHandler(Handler handler);
	String getName();
}
