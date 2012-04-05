package org.shivas.common.maths;

public interface LimitedValue {
	int current();
	void current(int current);
	void plus(int p);
	void minus(int m);
	void percent(int percent);
	
	int min();
	int max();
	
	void setMin();
	void setMax();
}
