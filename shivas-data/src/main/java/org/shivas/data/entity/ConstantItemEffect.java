package org.shivas.data.entity;

import java.io.Serializable;

import org.shivas.protocol.client.enums.ItemEffectEnum;

public class ConstantItemEffect implements Serializable, ItemEffect {

	private static final long serialVersionUID = -5447606535436769040L;
	
	private ItemEffectEnum effect;
	private short bonus;
	
	public ConstantItemEffect() {
	}
	
	public ConstantItemEffect(ItemEffectEnum effect, short bonus) {
		this.effect = effect;
		this.bonus = bonus;
	}

	/**
	 * @return the effect
	 */
	public ItemEffectEnum getType() {
		return effect;
	}
	/**
	 * @param effect the effect to set
	 */
	public void setType(ItemEffectEnum effect) {
		this.effect = effect;
	}
	/**
	 * @return the bonus
	 */
	public short getBonus() {
		return bonus;
	}
	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(short bonus) {
		this.bonus = bonus;
	}
	
	public ConstantItemEffect copy() {
		return new ConstantItemEffect(effect, bonus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConstantItemEffect other = (ConstantItemEffect) obj;
		if (bonus != other.bonus)
			return false;
		if (effect != other.effect)
			return false;
		return true;
	}

}
