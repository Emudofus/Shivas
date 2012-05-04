package org.shivas.server.core.statistics;

import org.shivas.common.statistics.CharacteristicType;

public class PlayerLifeValue extends LifeValue {
	
	private PlayerStatistics stats;

	public PlayerLifeValue(PlayerStatistics stats) {
		super();
		this.stats = stats;
	}

	public PlayerLifeValue(int current, PlayerStatistics stats) {
		super(current);
		this.stats = stats;
	}

	@Override
	public int max() {
		return (stats.owner().getExperience().level() - 1) * 5 + stats.get(CharacteristicType.Vitality).safeTotal();
	}

}
