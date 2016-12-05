package org.shivas.data.entity;

import org.shivas.common.random.Dice;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.types.BaseItemEffectType;

import java.io.Serializable;

public class WeaponItemEffect extends ItemEffect implements Serializable {

	private static final long serialVersionUID = -249953587810140756L;

	private Dice dice;

    public WeaponItemEffect(ItemEffectEnum type) {
        super(type);
    }

	public WeaponItemEffect(ItemEffectEnum type, Dice dice) {
        super(type);
		this.dice = dice;
	}

	public Dice getDice() {
		return dice;
	}

	public void setDice(Dice dice) {
		this.dice = dice;
	}

	@Override
	public WeaponItemEffect copy() {
		return new WeaponItemEffect(type);
	}

    @Override
    public BaseItemEffectType toBaseItemEffectType() {
        return new BaseItemEffectType(type, dice);
    }

    @Override
    public String toString(int radix) {
        return Integer.toString(type.value(), radix) + "," +
               dice.toString(radix);
    }

    @Override
    public void fromString(String string, int radix) {
        String[] args = string.split(",");

        type = ItemEffectEnum.valueOf(Integer.parseInt(args[0], radix));
        dice = Dofus1Dice.parseDice(args[1], radix);
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WeaponItemEffect that = (WeaponItemEffect) o;

		return dice != null ? dice.equals(that.dice) : that.dice == null;
	}

	@Override
	public int hashCode() {
		return dice != null ? dice.hashCode() : 0;
	}
}
