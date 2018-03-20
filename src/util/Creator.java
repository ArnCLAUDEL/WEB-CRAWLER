package util;

import io.MySerialisable;

public interface Creator<T extends MySerialisable> {
	public T init();
}