package org.shivas.data.entity;

public interface ItemAction {
	int getType();
	
	boolean able(Object target);
	void apply(Object target);
}
