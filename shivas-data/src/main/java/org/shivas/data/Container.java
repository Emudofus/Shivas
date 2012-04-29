package org.shivas.data;

public interface Container {
	<T> Repository<T> get(Class<T> entity);
}
