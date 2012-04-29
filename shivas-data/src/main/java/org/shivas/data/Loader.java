package org.shivas.data;

public interface Loader {
	<T> void load(Class<T> entity, String path);
	
	Container create();
}
