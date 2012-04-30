package org.shivas.data.repository;

import java.util.Collection;
import java.util.Map;

import org.shivas.data.Repository;

public class BaseRepository<T> implements Repository<T> {
	
	private Class<T> clazz;
	private Map<Integer, T> entities;
	
	public BaseRepository(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public void put(int id, T entity) {
		entities.put(id, entity);
	}

	@Override
	public T byId(int id) {
		return entities.get(id);
	}

	@Override
	public Class<T> getEntityClass() {
		return clazz;
	}

	@Override
	public Collection<T> all() {
		return entities.values();
	}

}
