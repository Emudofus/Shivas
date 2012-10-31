package org.shivas.core.core.statistics;

import org.shivas.common.maths.LimitedValue;

public abstract class LifeValue implements LimitedValue {
	
	protected int current;
	
	public LifeValue() {
	}
	
	public LifeValue(int current) {
		this.current = current;
		check();
	}

	protected void check() {
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
	public int plus(int p) {
        int old = current;
		current += p;
		check();
        return current - old;
	}

	@Override
	public int minus(int m) {
        int old = current;
		current -= m;
		check();
        return old - current;
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
	public int setMin() {
        int old = current;
		current = 0;
        return old;
	}

	@Override
	public int setMax() {
        int old = current;
		current = max();
        return current - old;
	}

    public abstract LifeValue copy();

}
