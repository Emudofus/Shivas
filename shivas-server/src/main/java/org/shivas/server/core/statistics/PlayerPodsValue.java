package org.shivas.server.core.statistics;

import org.shivas.common.statistics.CharacteristicType;

public class PlayerPodsValue extends PodsValue {
	
	private PlayerStatistics stats;

	public PlayerPodsValue(PlayerStatistics stats) {
		this.stats = stats;
	}

	@Override
	public int current() {
		return 0; // TODO items
	}

	@Override
	public int max() {
		return 1000 + stats.get(CharacteristicType.Strength).safeTotal() * 5; // TODO jobs
	}

}
