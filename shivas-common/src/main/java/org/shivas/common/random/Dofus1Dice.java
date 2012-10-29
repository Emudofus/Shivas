package org.shivas.common.random;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Dofus1Dice implements Dice {
	
	public static Dofus1Dice ZERO = new Dofus1Dice(0, 0, 0);
	
	private static final AtomicReference<Random> rand = new AtomicReference<Random>(new Random(System.nanoTime()));
	
	// EXAMPLE : 1d10+5
	
	public static Dofus1Dice parseDice(String string) {
		return parseDice(string, 10);
	}
	
	public static Dofus1Dice parseDice(String string, int radix) {
		int a = string.indexOf('d'),
			b = string.indexOf('+');

		int round = Integer.parseInt(string.substring(0, a), radix),
			num   = b >= 0 ? Integer.parseInt(string.substring(a + 1, b), radix) : Integer.parseInt(string.substring(a + 1), radix),
			add   = b >= 0 ? Integer.parseInt(string.substring(b + 1), radix) : 0;
			
		return new Dofus1Dice(round, num, add);
	}
	
	private int round, num, add;
	
	public Dofus1Dice() {
	}

	public Dofus1Dice(int round, int num) {
		this.round = round;
		this.num = num;
	}

	public Dofus1Dice(int round, int num, int add) {
		this.round = round;
		this.num = num;
		this.add = add;
	}

	public int min() {
		return add + 1;
	}

	public int max() {
		return round * num + add;
	}

	public int roll() {
		if (round <= 0 || num <= 0) return add;
		return rand.get().nextInt(round * num) + add;
	}

    @Override
    public Dice copy() {
        return new Dofus1Dice(round, num, add);
    }

    @Override
    public void upgrade(int upgrade) {
        add += upgrade;
    }

    public String toString() {
		return toString(10);
	}

	@Override
	public String toString(int radix) {
		return add > 0 ? 
				(Integer.toString(round, radix) + "d" + Integer.toString(num, radix) + "+" + Integer.toString(add, radix)) : 
				(Integer.toString(round, radix) + "d" + Integer.toString(num, radix));
	}

}
