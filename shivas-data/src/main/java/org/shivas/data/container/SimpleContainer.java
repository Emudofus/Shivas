package org.shivas.data.container;

import java.util.Map;

import org.shivas.data.Container;
import org.shivas.data.Repository;

import com.google.common.collect.Maps;

public class SimpleContainer implements Container {
	
	private Map<Class<?>, Repository<?>> repositories = Maps.newHashMap();

	@SuppressWarnings("unchecked")
	@Override
	public <T> Repository<T> get(Class<T> entity) {
		Repository<T> result = (Repository<T>) repositories.get(entity);
		if (result != null) {
			return result;
		}
		
		for (Map.Entry<Class<?>, Repository<?>> entry : repositories.entrySet()) {
			if (entry.getKey().getSuperclass() == entity) {
				return (Repository<T>) entry.getValue();
			}
		}
		return null;
	}
	
	public <T> void add(Repository<T> repo) {
		repositories.put(repo.getEntityClass(), repo);
	}

}
