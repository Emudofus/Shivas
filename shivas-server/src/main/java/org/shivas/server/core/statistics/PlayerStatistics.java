package org.shivas.server.core.statistics;

import java.util.Map;

import org.shivas.common.maths.LimitedValue;
import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;
import org.shivas.server.database.models.Player;

import com.google.common.collect.Maps;

public class PlayerStatistics implements Statistics {
	
	private Player owner;
	private LimitedValue life, pods = new PlayerPodsValue(this);
	private Map<CharacteristicType, Characteristic> characs = Maps.newHashMap();
	
	public PlayerStatistics(Player owner) {
		this(owner, 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
	}
	
	public PlayerStatistics(Player owner, int life, short actionPoints, short movementPoints, short initiative, short strength, short intelligence, short chance, short agility) {
		this.owner = owner;
		this.life = life <= 0 ? new PlayerLifeValue(this) : new PlayerLifeValue(life, this);
		for (CharacteristicType charac : CharacteristicType.values()) {
			switch (charac) {
			case ActionPoints:
				characs.put(charac, new BaseCharacteristic(charac, actionPoints));
				break;
				
			case MovementPoints:
				characs.put(charac, new BaseCharacteristic(charac, movementPoints));
				break;
			
			case Initiative:
				characs.put(charac, new InitiativeCharacteristic(this, initiative));
				break;
				
			case Prospection:
				characs.put(charac, new ProspectionCharacteristic(this));
				break;
				
			case Strength:
				characs.put(charac, new BaseCharacteristic(charac, strength));
				break;
				
			case Intelligence:
				characs.put(charac, new BaseCharacteristic(charac, intelligence));
				break;
				
			case Chance:
				characs.put(charac, new BaseCharacteristic(charac, chance));
				break;
				
			case Agility:
				characs.put(charac, new BaseCharacteristic(charac, agility));
				break;
				
			default:
				characs.put(charac, new BaseCharacteristic(charac));
				break;
			}
		}
	}

	public Player owner() {
		return owner;
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
		return characs.get(charac);
	}

}
