package org.shivas.core.core.statistics;

import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;

public class InitiativeCharacteristic extends BaseCharacteristic {

	private Statistics stats;

	public InitiativeCharacteristic(Statistics stats, short base, short equipment, short gift, short context) {
		super(CharacteristicType.Initiative, base, equipment, gift, context);
		this.stats = stats;
	}

	public InitiativeCharacteristic(Statistics stats, short base) {
		super(CharacteristicType.Initiative, base);
		this.stats = stats;
	}

	public InitiativeCharacteristic(Statistics stats) {
		super(CharacteristicType.Initiative);
		this.stats = stats;
	}

	@Override
	public short total() {
		short total = (short) (stats.get(CharacteristicType.Strength).safeTotal() +
				               stats.get(CharacteristicType.Intelligence).safeTotal() +
				               stats.get(CharacteristicType.Chance).safeTotal() +
				               stats.get(CharacteristicType.Agility).safeTotal());
		
		total += super.total();
		
		total *= (stats.life().current() / stats.life().max());
		
		return total;
	}

	@Override
	public Characteristic copy() {
		return new InitiativeCharacteristic(stats, base(), equipment(), gift(), context());
	}
	
}
