package org.shivas.data.entity;

public interface Action {
	String getName();
	
	boolean able(Object target);
	void apply(Object target);
}
