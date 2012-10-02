package org.shivas.data.entity;

import org.shivas.common.random.Dice;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.types.BaseItemTemplateEffectType;

import java.io.Serializable;

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
		return effect.isWeaponEffect() ?
				new WeaponItemEffect(effect, bonus) :
				new ConstantItemEffect(effect, (short) bonus.roll());
	}

    public BaseItemTemplateEffectType toBaseItemTemplateEffectType() {
        return new BaseItemTemplateEffectType(
                effect,
                0, 0, 0,
                bonus
        );
    }

}
