package org.shivas.server.core.statistics;

import org.shivas.common.maths.LimitedValue;
import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;
import org.shivas.server.database.models.Player;

public class PlayerStatistics implements Statistics {
	
	private Player owner;
	private LimitedValue life, pods;
	
	public PlayerStatistics() {
	}

	public Player owner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@Override
	public LimitedValue life() {
		return life;
	}

	@Override
	public LimitedValue pods() {
		return pods;
	}

	@Override
	public Characteristic get(CharacteristicType charac) {
		// TODO Auto-generated method stub
		return null;
	}

}
