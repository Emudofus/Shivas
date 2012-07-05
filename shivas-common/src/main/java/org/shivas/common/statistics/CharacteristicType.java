package org.shivas.common.statistics;

import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 20:01
 * IDE : IntelliJ IDEA
 */
public enum CharacteristicType {
	Life,
	Pods,
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
    RangePoints,
    Summons,
    Damage,
    PhysicalDamage,
    WeaponControl,
    DamagePer,
    HealPoints,
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

    ResistanceEarth,
    ResistancePercentEarth,
    ResistancePvpEarth,
    ResistancePercentPvpEarth,

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

    private static HashMap<Integer, CharacteristicType> values = new HashMap<Integer, CharacteristicType>();
    static{
        for (CharacteristicType value : values()){
            values.put(value.ordinal(), value);
        }
    }
    public static CharacteristicType valueOf(int ordinal){
        return values.get(ordinal);
    }
}
