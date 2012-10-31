package org.shivas.core.core.statistics;

import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;

public class ProspectionCharacteristic extends BaseCharacteristic {
	
	private PlayerStatistics stats;

	public ProspectionCharacteristic(PlayerStatistics stats) {
		super(CharacteristicType.Prospection);
		this.stats = stats;
	}

	public ProspectionCharacteristic(PlayerStatistics stats, short equipment, short gift, short context) {
		super(CharacteristicType.Prospection, (short)0, equipment, gift, context);
		this.stats = stats;
	}

	@Override
	public short base() {
		return stats.owner().getBreed().getStartProspection();
	}

	@Override
	public void base(short base) { }

	@Override
	public void plusBase(short base) { }

	@Override
	public void minusBase(short minus) { }

	@Override
	public short total() {
		return (short) (super.total() + stats.get(CharacteristicType.Chance).safeTotal() / 10);
	}

	@Override
	public Characteristic copy(Statistics copiedStats) {
		return new ProspectionCharacteristic((PlayerStatistics) copiedStats, equipment(), gift(), context());
	}
	
}
