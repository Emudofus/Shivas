package org.shivas.core.core.statistics;

import org.shivas.common.statistics.CharacteristicType;

public class PlayerLifeValue extends LifeValue {
	
	private PlayerStatistics stats;
	private int max;

	public PlayerLifeValue(PlayerStatistics stats) {
		this.stats = stats;
		this.max = stats.owner().getBreed().getStartLife();
		this.current = max();
	}

	public PlayerLifeValue(int current, PlayerStatistics stats) {
		this.stats = stats;
		this.max = stats.owner().getBreed().getStartLife();
		this.current = current;
		check();
	}

	@Override
	public int max() {
		return max + stats.get(CharacteristicType.Vitality).safeTotal();
	}

	@Override
	public void plusMax(int p) {
		max += p;
		current += p;
	}

	@Override
	public void minusMax(int m) {
		max -= m;
		current -= m;
	}

	@Override
	public void resetMax() {
		int diff = stats.owner().getBreed().getStartLife() - max;
		max = stats.owner().getBreed().getStartLife();
		current += diff;
	}

}
