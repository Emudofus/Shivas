package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 12:58
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum ItemPositionEnum {
    NotEquiped(-1),
	Amulet(0),
	Weapon(1),
	LeftRing(2),
	Belt(3),
	RightRing(4),
	Boot(5),
	Hat(6),
	Cloak(7),
	Pet(8),
	Dofus1(9),
	Dofus2(10),
	Dofus3(11),
	Dofus4(12),
	Dofus5(13),
	Dofus6(14),
	Shield(15),
	ItemBar1(23),
	ItemBar2(24),
	ItemBar3(25),
	ItemBar4(26),
	ItemBar5(27),
	ItemBar6(28),
	ItemBar7(29),
	ItemBar8(30),
	ItemBar9(31),
	ItemBar10(32),
	ItemBar11(33),
	ItemBar12(34),
	ItemBar13(35),
	ItemBar14(36);

    private int value;

    private ItemPositionEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public boolean equipment(){
        return this != ItemPositionEnum.NotEquiped &&
               this != ItemPositionEnum.ItemBar1   &&
               this != ItemPositionEnum.ItemBar2   &&
               this != ItemPositionEnum.ItemBar3   &&
               this != ItemPositionEnum.ItemBar4   &&
               this != ItemPositionEnum.ItemBar5   &&
               this != ItemPositionEnum.ItemBar6   &&
               this != ItemPositionEnum.ItemBar7   &&
               this != ItemPositionEnum.ItemBar8   &&
               this != ItemPositionEnum.ItemBar9   &&
               this != ItemPositionEnum.ItemBar10  &&
               this != ItemPositionEnum.ItemBar11  &&
               this != ItemPositionEnum.ItemBar12  &&
               this != ItemPositionEnum.ItemBar13  &&
               this != ItemPositionEnum.ItemBar14;
    }

    private static final Map<Integer, ItemPositionEnum> values = new HashMap<Integer, ItemPositionEnum>();

    static {
        for (ItemPositionEnum e : values()) {
            values.put(e.value(), e);
        }
    }

    public static ItemPositionEnum valueOf(int value) {
        return values.get(value);
    }
}
