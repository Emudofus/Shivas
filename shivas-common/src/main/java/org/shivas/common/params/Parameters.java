package org.shivas.common.params;

import com.google.common.collect.Maps;

import java.util.Map;

public class Parameters {

	private Map<String, Object> values = Maps.newHashMap();
	
	public Parameters(Map<String, Object> values) {
		this.values = values;
	}

	public <T> T get(String name, Class<T> clazz) {
		return clazz.cast(values.get(name));
	}
	
	public Object get(String name) {
		return values.get(name);
	}

    public boolean has(String name) {
        return values.containsKey(name);
    }

    public boolean has(String name, Class<?> clazz) {
        Object value = values.get(name);
        return value != null && clazz.isInstance(value);
    }
	
}
