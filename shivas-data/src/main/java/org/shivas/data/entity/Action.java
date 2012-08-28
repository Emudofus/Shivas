package org.shivas.data.entity;

public interface Action {
	int getType();
	
	boolean able(Object target);
	void apply(Object target);
}
