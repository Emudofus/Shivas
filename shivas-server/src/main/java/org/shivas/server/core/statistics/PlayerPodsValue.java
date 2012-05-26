package org.shivas.server.core.statistics;

import org.shivas.common.statistics.CharacteristicType;
import org.shivas.server.database.models.GameItem;

public class PlayerPodsValue extends PodsValue {
	
	private PlayerStatistics stats;

	public PlayerPodsValue(PlayerStatistics stats) {
		this.stats = stats;
	}

	@Override
	public int current() {
		int current = 0;
		for (GameItem item : stats.owner().getBag()) {
			current += item.getTemplate().getWeight();
		}
		return current;
	}

	@Override
	public int max() {
		return 1000 + stats.get(CharacteristicType.Strength).safeTotal() * 5; // TODO jobs
	}

}
