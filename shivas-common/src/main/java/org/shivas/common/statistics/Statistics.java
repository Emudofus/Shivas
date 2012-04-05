package org.shivas.common.statistics;

import org.shivas.common.maths.LimitedValue;

public interface Statistics {
	LimitedValue life();
	LimitedValue pods();
	
	Characteristic get(CharacteristicType charac);
}
