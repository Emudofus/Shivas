package org.shivas.core.core.statistics;

import org.shivas.common.maths.BasicLimitedValue;

public class EnergyValue extends BasicLimitedValue {

	private static final int MIN_ENERGY = 0;
	private static final int MAX_ENERGY = 10000;
	
	public EnergyValue() {
		this(MAX_ENERGY);
	}

	public EnergyValue(int current) {
		super(current, MIN_ENERGY, MAX_ENERGY);
	}

}
