package org.shivas.common.random;

import java.util.Random;

public interface Dice {
	int min();
	int max();
	
	int roll(Random rand);

    Dice copy();

    void upgrade(int upgrade);
	
	String toString(int radix);
}
