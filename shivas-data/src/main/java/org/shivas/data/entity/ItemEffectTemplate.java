package org.shivas.data.entity;

import java.io.Serializable;

import org.shivas.common.random.Dice;
import org.shivas.protocol.client.enums.ItemEffectEnum;

public class ItemEffectTemplate implements Serializable {

	private static final long serialVersionUID = 6265355183966967471L;
	
	private ItemEffectEnum effect;
	private Dice bonus;
	
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
	public Dice getBonus() {
		return bonus;
	}
	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(Dice bonus) {
		this.bonus = bonus;
	}
	
	public ItemEffect generate() {
		return new ItemEffect(effect, (short) bonus.roll());
	}

}
