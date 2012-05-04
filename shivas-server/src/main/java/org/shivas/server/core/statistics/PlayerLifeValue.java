package org.shivas.server.core.statistics;

import org.shivas.common.statistics.CharacteristicType;

public class PlayerLifeValue extends LifeValue {
	
	private PlayerStatistics stats;

	public PlayerLifeValue(PlayerStatistics stats) {
		this.stats = stats;
		this.current = max();
	}

	public PlayerLifeValue(int current, PlayerStatistics stats) {
		this.stats = stats;
		this.current = current;
		check();
		
	}

	@Override
	public int max() {
		return (stats.owner().getExperience().level() - 1) * 5 + stats.get(CharacteristicType.Vitality).safeTotal();
	}

}
