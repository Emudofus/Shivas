package org.shivas.common.random;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Dofus1Dice implements Dice {
	
	// EXAMPLE : 1d10+5
	
	public static Dofus1Dice parseDice(String string) {
		int a = string.indexOf('d'),
			b = string.indexOf('+');

		int round = Integer.parseInt(string.substring(0, a)),
			num   = b >= 0 ? Integer.parseInt(string.substring(a + 1, b)) : Integer.parseInt(string.substring(a + 1)),
			add   = b >= 0 ? Integer.parseInt(string.substring(b + 1)) : 0;
			
		return new Dofus1Dice(round, num, add);
	}
	
	private int round, num, add;
	
	private AtomicReference<Random> rand = new AtomicReference<Random>(new Random(System.nanoTime()));
	
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
		return add;
	}

	public int max() {
		return round * num + add;
	}

	public int roll() {
		return rand.get().nextInt(round * num) + add;
	}
	
	public String toString() {
		return add > 0 ? 
				(round + "d" + num + "+" + add) : 
				(round + "d" + num);
	}

}
