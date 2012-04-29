package org.shivas.data.container;

import java.util.Map;

import org.shivas.data.Container;
import org.shivas.data.Repository;

public class SimpleContainer implements Container {
	
	private Map<Class<?>, Repository<?>> repositories;

	@SuppressWarnings("unchecked")
	@Override
	public <T> Repository<T> get(Class<T> entity) {
		return (Repository<T>) repositories.get(entity);
	}
	
	public <T> void add(Repository<T> repo) {
		repositories.put(repo.getEntityClass(), repo);
	}

}
