package org.shivas.common.params;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

public class Conditions implements Iterable<Condition> {
	
	public static final Conditions EMPTY = new Conditions();

	private Map<String, Condition> conditions = Maps.newHashMap();
	
	public void add(Condition condition) {
		conditions.put(condition.getName(), condition);
	}
	
	public void add(String name, Type type, String help, boolean optional) {
		add(new Condition(name, type, help, optional));
	}
	
	public void add(String name, Type type, String help) {
		add(name, type, help, false);
	}
	
	public Condition get(String name) {
		return conditions.get(name);
	}
	
	public Map<String, Condition> asMap() {
		return Maps.newHashMap(conditions);
	}

	@Override
	public Iterator<Condition> iterator() {
		return conditions.values().iterator();
	}
	
}
