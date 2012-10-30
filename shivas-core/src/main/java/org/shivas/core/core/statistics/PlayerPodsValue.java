package org.shivas.core.core.statistics;

import org.shivas.common.statistics.CharacteristicType;
import org.shivas.core.database.models.GameItem;

public class PlayerPodsValue extends PodsValue {
	
	private PlayerStatistics stats;
	private int max;

	public PlayerPodsValue(PlayerStatistics stats) {
		this.stats = stats;
		this.max = 1000;
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
		return max + stats.get(CharacteristicType.Strength).safeTotal() * 5; // TODO jobs
	}

	@Override
	public void plusMax(int p) {
		max += p;
	}

	@Override
	public void minusMax(int m) {
		max -= m;
	}

	@Override
	public void resetMax() {
		max = 1000;
	}

}
