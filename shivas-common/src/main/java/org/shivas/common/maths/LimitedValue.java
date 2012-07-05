package org.shivas.common.maths;

public interface LimitedValue {
	int current();
	void setCurrent(int current);
	void plus(int p);
	void minus(int m);
	void percent(int percent);
	
	int min();
	int max();
	
	void plusMax(int p);
	void minusMax(int p);
	void resetMax();
	
	void setMin();
	void setMax();
}
