package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 24/11/11
 * Time: 18:53
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum ActionTypeEnum {
    MOVEMENT(1),
    MAP_CHANGEMENT(2),
    CELL_CHANGEMENT(4),
    CELL_SLIDE(5),
    LIFE_CHANGEMENT(100),
    AP_CHANGEMENT(102),
    KILL_UNIT(103),
    BLOCKED_DAMAGE(105),
    MP_CHANGEMENT(129),
    CLEAR_BUFFS(132),
    INVISIBLE(150),
    SUMMONED(181),
    CAST_SPELL(300),
    SPELL_CRITICAL(301),
    SPELL_FAILURE(302),
    MELEE_ATTACK(303),
    INTERACTIVE_OBJECT(500),
    ASK_FIGHT(900),
    ACCEPT_FIGHT(901),
    DECLINE_FIGHT(902),
    JOIN_FIGHT(903),
    FIGHT_AGGRESSION(906),
    FIGHTER_STATE(950),
    TURN_LIST(999);

    private int value;

    private ActionTypeEnum(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }

    private static final Map<Integer, ActionTypeEnum> values = new HashMap<Integer, ActionTypeEnum>();

    static {
        for (ActionTypeEnum e : values()) {
            values.put(e.value(), e);
        }
    }

    public static ActionTypeEnum valueOf(int ordinal) {
        return values.get(ordinal);
    }
}
