package org.shivas.protocol.client.types;

import org.shivas.common.random.Dice;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.protocol.client.enums.ItemEffectEnum;

/**
 * User: Blackrush
 * Date: 26/12/11
 * Time: 20:50
 * IDE : IntelliJ IDEA
 */
public class BaseItemEffectType {
    private ItemEffectEnum effect;
    private short bonus;
    private Dice dice;

    public BaseItemEffectType(ItemEffectEnum effect, short bonus, Dice dice) {
        this.effect = effect;
        this.bonus = bonus;
        this.dice = dice;
    }

    public BaseItemEffectType(ItemEffectEnum effect, short bonus) {
    	this(effect, bonus, Dofus1Dice.ZERO);
	}
    
    public BaseItemEffectType(ItemEffectEnum effect, Dice dice) {
    	this(effect, (short) 0, dice);
    }

	public ItemEffectEnum getEffect() {
        return effect;
    }

    public void setEffect(ItemEffectEnum effect) {
        this.effect = effect;
    }

    public short getBonus() {
        return bonus;
    }

    public void setBonus(short bonus) {
        this.bonus = bonus;
    }

	public Dice getDice() {
		return dice;
	}

	public void setDice(Dice dice) {
		this.dice = dice;
	}
}
