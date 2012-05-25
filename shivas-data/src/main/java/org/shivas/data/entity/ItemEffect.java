package org.shivas.data.entity;

import java.io.Serializable;

import org.shivas.protocol.client.enums.ItemEffectEnum;

public class ItemEffect implements Serializable {

	private static final long serialVersionUID = -5447606535436769040L;
	
	private ItemEffectEnum effect;
	private short bonus;
	
	public ItemEffect() {
	}
	
	public ItemEffect(ItemEffectEnum effect, short bonus) {
		this.effect = effect;
		this.bonus = bonus;
	}

	/**
	 * @return the effect
	 */
	public ItemEffectEnum getEffect() {
		return effect;
	}
	/**
	 * @param effect the effect to set
	 */
	public void setEffect(ItemEffectEnum effect) {
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
	
	public ItemEffect copy() {
		return new ItemEffect(effect, bonus);
	}

}
