package org.shivas.data.entity;

import org.shivas.common.random.Dice;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.types.BaseItemEffectType;

public abstract class ItemEffect {
    public static ItemEffect valueOf(String string, int radix) {
        int index = string.indexOf(',');
        if (index < 0) return null;

        ItemEffectEnum effect = ItemEffectEnum.valueOf(Integer.parseInt(string.substring(0, index), radix));

        if (!effect.isWeaponEffect()) {
            short bonus = Short.parseShort(string.substring(index + 1), radix);

            return new ConstantItemEffect(effect, bonus);
        } else {
            Dice dice = Dofus1Dice.parseDice(string.substring(index + 1), radix);

            return new WeaponItemEffect(effect, dice);
        }
    }

    protected ItemEffectEnum type;

    protected ItemEffect(ItemEffectEnum type) {
        this.type = type;
    }

    public ItemEffectEnum getType() {
        return type;
    }

	public void setType(ItemEffectEnum type) {
        this.type = type;
    }
	
	public abstract ItemEffect copy();

    public abstract BaseItemEffectType toBaseItemEffectType();
    public abstract String toString(int radix);
    public abstract void fromString(String string, int radix);

    public void fromString(String string) {
        fromString(string, 10);
    }
}
