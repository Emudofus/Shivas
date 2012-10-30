package org.shivas.core.core.statistics;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import org.shivas.common.maths.LimitedValue;
import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;
import org.shivas.data.entity.ConstantItemEffect;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemSet;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.core.database.models.GameItem;
import org.shivas.core.database.models.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerStatistics implements Statistics {
	
	private Player owner;
	
	private LifeValue life;
	private LimitedValue energy;
	private PodsValue pods = new PlayerPodsValue(this);
	
	private short statPoints, spellPoints;
	private Map<CharacteristicType, Characteristic> characs = Maps.newHashMap();
	
	public PlayerStatistics(Player owner, short actionPoints, short movementPoints, short vitality, short wisdom, short strength, short intelligence, short chance, short agility) {
		this(
				owner,
				(short)((owner.getExperience().level() - 1) * 5),
				(short)(owner.getExperience().level() - 1),
				-1, -1, 
				actionPoints, movementPoints,
				vitality, wisdom, strength, intelligence, chance, agility
		);
	}
	
	public PlayerStatistics(Player owner, short statPoints, short spellPoints, int energy, int life, short actionPoints, short movementPoints, short vitality, short wisdom, short strength, short intelligence, short chance, short agility) {
		this.owner = owner;
		this.statPoints = statPoints;
		this.spellPoints = spellPoints;
		for (CharacteristicType charac : CharacteristicType.values()) {
			switch (charac) {
			case ActionPoints:
				characs.put(charac, new BaseCharacteristic(charac, actionPoints));
				break;
				
			case MovementPoints:
				characs.put(charac, new BaseCharacteristic(charac, movementPoints));
				break;
			
			case Initiative:
				characs.put(charac, new InitiativeCharacteristic(this));
				break;
				
			case Prospection:
				characs.put(charac, new ProspectionCharacteristic(this));
				break;
				
			case Vitality:
				characs.put(charac, new VitalityCharacteristic(this, charac, vitality));
				break;
				
			case Wisdom:
				characs.put(charac, new BaseCharacteristic(charac, wisdom));
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
				
			case Summons:
				characs.put(charac, new BaseCharacteristic(charac, (short) 1));
				break;
				
			case Life:
			case Pods:
				break;
				
			default:
				characs.put(charac, new BaseCharacteristic(charac));
				break;
			}
		}
		this.energy = energy < 0 ? new EnergyValue() : new EnergyValue(energy);
		this.life = life < 0 ? new PlayerLifeValue(this) : new PlayerLifeValue(life, this);
	}

	public Player owner() {
		return owner;
	}
	
	@Override
	public LifeValue life() {
		return life;
	}

	@Override
	public LimitedValue pods() {
		return pods;
	}
	
	public LimitedValue energy() {
		return energy;
	}

	public short statPoints() {
		return statPoints;
	}

	public void setStatPoints(short statPoints) {
		this.statPoints = statPoints;
	}
	
	public void addStatPoints(short statPoints) {
		this.statPoints += statPoints;
	}

	public short spellPoints() {
		return spellPoints;
	}

	public void setSpellPoints(short spellPoints) {
		this.spellPoints = spellPoints;
	}
	
	public void addSpellPoints(short spellPoints) {
		this.spellPoints += spellPoints;
	}

	@Override
	public Characteristic get(CharacteristicType charac) {
		return characs.get(charac);
	}

    @Override
    public void resetAll() {
        for (Characteristic charac : characs.values()) {
            charac.base((short) 0);
            charac.equipment((short) 0);
            charac.gift((short) 0);
            charac.context((short) 0);
        }
    }

    @Override
    public void resetBase() {
        for (Characteristic charac : characs.values()) {
            charac.base((short) 0);
        }
    }

    @Override
    public void resetEquipment() {
        for (Characteristic charac : characs.values()) {
            charac.equipment((short) 0);
        }
    }

    @Override
    public void resetGift() {
        for (Characteristic charac : characs.values()) {
            charac.gift((short) 0);
        }
    }

    @Override
    public void resetContext() {
        for (Characteristic charac : characs.values()) {
            charac.context((short) 0);
        }
    }

    public String packet() {
		return GameMessageFormatter.statisticsMessage(
				owner.getExperience().current(),
				owner.getExperience().min(),
				owner.getExperience().max(),
				owner.getBag().getKamas(),
				statPoints,
				spellPoints,
				0, (short) 0, (short) 0, 0, 0, false, // TODO pvp
				energy,
				this
		);
	}
	
	public PlayerStatistics reset() {
		life.resetMax();
		pods.resetMax();
		
		for (Characteristic charac : characs.values()) {
			charac.equipment((short) 0);
			charac.gift((short) 0);
			charac.context((short) 0);
		}
		
		return this;
	}
	
	protected LimitedValue getValue(CharacteristicType charac) {
		switch (charac) {
		case Life:
			return life;
			
		case Pods:
			return pods;
			
		default:
			return null;
		}
	}
	
	protected void apply(ConstantItemEffect effect) {
		AtomicReference<Boolean> add = new AtomicReference<Boolean>(false);
		
		CharacteristicType characType = effect.getType().toCharacteristicType(add);
		if (characType == null) return;
		
		Characteristic charac = get(characType);
		
		if (charac != null) {
			if (add.get()) 	charac.plusEquipment(effect.getBonus());
			else 			charac.minusEquipment(effect.getBonus());
		} else {
			LimitedValue value = getValue(characType);
			if (value != null) {
				if (add.get()) value.plusMax(effect.getBonus());
				else		   value.minusMax(effect.getBonus());
			}
		}
	}
	
	public PlayerStatistics refresh() {
		reset();

		Multiset<ItemSet> multiset = HashMultiset.create();
		for (GameItem item : owner.getBag().equiped()) {
			if (item.getTemplate().getItemSet() != null) {
				multiset.add(item.getTemplate().getItemSet());
			}
			
			for (ItemEffect effect : item.getItemEffects()) {
				if (effect instanceof ConstantItemEffect)
					apply((ConstantItemEffect) effect);
			}
		}
		
		for (Multiset.Entry<ItemSet> entry : multiset.entrySet()) {	
			Collection<ConstantItemEffect> effects = entry.getElement().getEffects(entry.getCount());
			
			for (ConstantItemEffect effect : effects) {
				apply(effect);
			}
		}
		
		return this;
	}

}
