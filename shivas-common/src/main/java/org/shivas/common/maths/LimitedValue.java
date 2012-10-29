package org.shivas.common.maths;

public interface LimitedValue {
	int current();
	void setCurrent(int current);

    /**
     * add to current
     * @param p to add
     * @return effectively added
     */
	int plus(int p);

    /**
     * remove from current
     * @param m to remove
     * @return effectively removed
     */
	int minus(int m);

	void percent(int percent);
	
	int min();
	int max();
	
	void plusMax(int p);
	void minusMax(int p);
	void resetMax();
	
	int setMin();
	int setMax();
}
