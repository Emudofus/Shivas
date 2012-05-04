package org.shivas.server.core.statistics;

import org.shivas.common.maths.LimitedValue;

public abstract class LifeValue implements LimitedValue {
	
	private int current;
	
	public LifeValue() {
		this.current = max();
	}
	
	public LifeValue(int current) {
		this.current = current;
		check();
	}

	private void check() {
		int max;
		if (current < 0) {
			current = 0;
		} else if (current > (max = max())) {
			current = max;
		}
	}

	@Override
	public int current() {
		return current;
	}

	@Override
	public void setCurrent(int current) {
		this.current = current;
		check();
	}

	@Override
	public void plus(int p) {
		this.current += p;
		check();
	}

	@Override
	public void minus(int m) {
		this.current -= m;
		check();
	}

	@Override
	public void percent(int percent) {
		int max = max();
		if (percent <= 0) {
			current = 0;
		} else if (percent >= 100) {
			current = max;
		}
		else {
			current = (max / 100) * percent;
		}
	}

	@Override
	public int min() {
		return 0;
	}

	@Override
	public void setMin() {
		current = 0;
	}

	@Override
	public void setMax() {
		current = max();
	}
	
}
