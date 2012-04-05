package org.shivas.protocol.client.enums;

import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 20:01
 * IDE : IntelliJ IDEA
 */
public enum CharacteristicsEnum {
    Prospection,
    Initiative,
    ActionPoints,
    MovementPoints,
    Strength,
    Vitality,
    Wisdom,
    Chance,
    Agility,
    Intelligence,
    Range,
    Summons,
    Damage,
    PhysicalDamage,
    WeaponControl,
    DamagePer,
    Soin,
    TrapDamage,
    TrapDamagePer,
    DamageReturn,
    CriticalHit,
    CriticalFailure,

    DodgeActionPoints,
    DodgeMovementPoints,

    ResistanceNeutral,
    ResistancePercentNeutral,
    ResistancePvpNeutral,
    ResistancePercentPvpNeutral,

    ResistanceGround,
    ResistancePercentGround,
    ResistancePvpGround,
    ResistancePercentPvpGround,

    ResistanceWater,
    ResistancePercentWater,
    ResistancePvpWater,
    ResistancePercentPvpWater,

    ResistanceWind,
    ResistancePercentWind,
    ResistancePvpWind,
    ResistancePercentPvpWind,

    ResistanceFire,
    ResistancePercentFire,
    ResistancePvpFire,
    ResistancePercentPvpFire;

    private static HashMap<Integer, CharacteristicsEnum> values = new HashMap<Integer, CharacteristicsEnum>();
    static{
        for (CharacteristicsEnum value : values()){
            values.put(value.ordinal(), value);
        }
    }
    public static CharacteristicsEnum valueOf(int ordinal){
        return values.get(ordinal);
    }
}
