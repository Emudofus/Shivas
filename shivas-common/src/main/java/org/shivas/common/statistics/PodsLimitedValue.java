package org.shivas.common.statistics;

import org.shivas.common.maths.LimitedValue;

public abstract class PodsLimitedValue implements LimitedValue {
	
	protected Statistics stats;

	public PodsLimitedValue(Statistics stats) {
		this.stats = stats;
	}

	public void current(int current) {
	}

	public void plus(int p) {
	}

	public void minus(int m) {
	}

	public void percent(int percent) {
	}

	public int min() {
		return 0;
	}

	public int max() {
		return 1000 + stats.get(CharacteristicType.Strength).total() * 5;
	}

	public void setMin() {
	}

	public void setMax() {
	}

}
