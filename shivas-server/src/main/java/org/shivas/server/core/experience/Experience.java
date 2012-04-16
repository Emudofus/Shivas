package org.shivas.server.core.experience;

public interface Experience<T extends Number> {

	short level();
	void addLevel(short level);
	void removeLevel(short level);
	
	T current();
	T min();
	T max();
	
	void plus(T experience);
	void minus(T experience);
	
}
