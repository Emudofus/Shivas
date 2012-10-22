package org.shivas.common.statistics;

import org.shivas.common.maths.LimitedValue;

public interface Statistics {
	LimitedValue life();
	LimitedValue pods();
	
	Characteristic get(CharacteristicType charac);

    void resetAll();
    void resetBase();
    void resetEquipment();
    void resetGift();
    void resetContext();
}
