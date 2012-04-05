package org.shivas.common.statistics;

import org.shivas.common.maths.BasicLimitedValue;

public class LifeLimitedValue extends BasicLimitedValue {
	
	public static int maxLife(int startLife, int level, Characteristic vitality) {
        return startLife + 5 * (level - 1) + vitality.safeTotal();
	}
	
	public LifeLimitedValue(int startLife, int level, Characteristic vitality) {
		super(maxLife(startLife, level, vitality), 0, maxLife(startLife, level, vitality));
	}
	
	public LifeLimitedValue(int current, int startLife, int level, Characteristic vitality) {
		super(current, 0, maxLife(startLife, level, vitality));
	}

}
