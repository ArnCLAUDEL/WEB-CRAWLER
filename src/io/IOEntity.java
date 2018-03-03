package io;

public interface IOEntity extends Runnable {
	public boolean isActive();
	public void shutdown();
	public void addHandler(AbstractHandler handler);
	public void removeHandler(AbstractHandler handler);
}
