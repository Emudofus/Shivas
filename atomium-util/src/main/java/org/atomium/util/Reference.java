package org.atomium.util;

public interface Reference<T> {

	T get();
	void set(T o);
	
	boolean isNull();
	
}
