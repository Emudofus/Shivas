package org.shivas.data;

public interface Repository<T> {
	T byId(int id);
	
	Class<T> getEntityClass();
}
