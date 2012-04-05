package org.shivas.common.maths;

public class BasicLimitedValue implements LimitedValue {
	
	private int current, min, max;
	
	public BasicLimitedValue(int max) {
		this.current = 0;
		this.min = 0;
		this.max = max;
	}

	public BasicLimitedValue(int min, int max) {
		this.current = min;
		this.min = min;
		this.max = max;
	}

	public BasicLimitedValue(int current, int min, int max) {
		this.current = current;
		this.min = min;
		this.max = max;
	}

	public int current() {
		return current;
	}

	public void current(int current) {
		if (current < min) {
			current = min;
		} else if (current > max) {
			current = max;
		}
		this.current = current;
	}

	public void plus(int p) {
		this.current += p;
		
		if (current < min) {
			current = min;
		} else if (current > max) {
			current = max;
		}
	}

	public void minus(int m) {
		this.current -= m;

		if (current < min) {
			current = min;
		} else if (current > max) {
			current = max;
		}
	}

	public void percent(int percent) {
		if (percent < 0) {
			current = 0;
		} else if (percent > 100) {
			current = max;
		} else {
			current = max * percent / 100;
		}
	}

	public int min() {
		return min;
	}

	public int max() {
		return max;
	}

	public void setMin() {
		this.current = min;
	}

	public void setMax() {
		this.current = max;
	}

}
