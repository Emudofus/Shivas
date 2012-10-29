package org.shivas.common.random;

public interface Dice {
	int min();
	int max();
	
	int roll();

    Dice copy();

    void upgrade(int upgrade);
	
	String toString(int radix);
}
