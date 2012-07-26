package org.shivas.data.entity;

import java.io.Serializable;

import org.shivas.common.random.Dice;
import org.shivas.protocol.client.enums.ItemEffectEnum;

public class WeaponItemEffect implements Serializable, ItemEffect {

	private static final long serialVersionUID = -249953587810140756L;
	
	private ItemEffectEnum type;
	private Dice dice;
	
	public WeaponItemEffect() {
	}

	public WeaponItemEffect(ItemEffectEnum type, Dice dice) {
		this.type = type;
		this.dice = dice;
	}

	@Override
	public ItemEffectEnum getType() {
		return type;
	}

	@Override
	public void setType(ItemEffectEnum type) {
		this.type = type;
	}

	public Dice getDice() {
		return dice;
	}

	public void setDice(Dice dice) {
		this.dice = dice;
	}

	@Override
	public WeaponItemEffect copy() {
		return new WeaponItemEffect();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeaponItemEffect other = (WeaponItemEffect) obj;
		if (dice == null) {
			if (other.dice != null)
				return false;
		} else if (!dice.equals(other.dice))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
