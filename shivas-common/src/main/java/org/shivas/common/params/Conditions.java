package org.shivas.common.params;

import java.util.Map;

import com.google.common.collect.Maps;

public class Conditions {

	private Map<String, Condition> conditions = Maps.newHashMap();
	
	public void add(Condition condition) {
		conditions.put(condition.getName(), condition);
	}
	
	public void add(String name, Type type, boolean optional) {
		add(new Condition(name, type, optional));
	}
	
	public void add(String name, Type type) {
		add(name, type, false);
	}
	
	public Condition get(String name) {
		return conditions.get(name);
	}
	
	public Map<String, Condition> asMap() {
		return Maps.newHashMap(conditions);
	}
	
}
