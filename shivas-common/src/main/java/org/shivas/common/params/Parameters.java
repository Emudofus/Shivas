package org.shivas.common.params;

import java.util.Map;

import com.google.common.collect.Maps;

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
	
}
