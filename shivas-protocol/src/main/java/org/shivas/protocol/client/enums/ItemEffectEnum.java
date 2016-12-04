package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 12:03
 * IDE : IntelliJ IDEA
 */

import org.shivas.common.statistics.CharacteristicType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public enum ItemEffectEnum {
    NONE(0),
    AddRenvoiDamage(107),
    Heal(108),
    AddAgility(119),
	AddChance(123),
	AddDamage(112),
	AddDamagePercent(138),
	AddCriticalHit(115),
	AddCriticalFailure(122),
	AddDodgeAP(160),
	AddDodgeMP(161),
	AddStrength(118),
	AddInitiative(174),
	AddIntelligence(126),
	AddSummons(182),
	AddLife(110),
	AddActionPoints(111),
	AddMovementPoints(128),
	AddRangePoints(117),
	AddPods(158),
	AddProspection(176),
	AddWisdom(124),
	AddHealPoints(178),
	AddVitality(125),
	MultiplyDamage(114),
	SubAgility(154),
	SubChance(152),
	SubDamage(164),
	SubCriticalHit(171),
	SubDamageMagic(172),
	SubDamagePhysic(173),
	SubDodgeAP(162),
	SubDodgeMP(163),
	SubStrength(157),
	SubInitiative(175),
	SubIntelligence(155),
	SubActionPoints(168),
	SubMovementPoints(169),
	SubRangePoints(116),
	SubPods(159),
	SubProspection(177),
	SubWisdom(156),
	SubHealPoints(179),
	SubVitality(153),

	AddReduceDamagePercentWater(211),
	AddReduceDamagePercentEarth(210),
	AddReduceDamagePercentWind(212),
	AddReduceDamagePercentFire(213),
	AddReduceDamagePercentNeutral(214),
	AddReduceDamagePercentPvPWater(251),
	AddReduceDamagePercentPvPEarth(250),
	AddReduceDamagePercentPvPWind(252),
	AddReduceDamagePercentPvPFire(253),
	AddReduceDamagePercentPvpNeutral(254),
	AddReduceDamageWater(241),
	AddReduceDamageEarth(240),
	AddReduceDamageWind(242),
	AddReduceDamageFire(243),
	AddReduceDamageNeutral(244),
	AddReduceDamagePvPWater(261),
	AddReduceDamagePvPEarth(260),
	AddReduceDamagePvPWind(262),
	AddReduceDamagePvPFire(263),
	AddReduceDamagePvPNeutral(264),

	SubReduceDamagePercentWater(216),
	SubReduceDamagePercentEarth(215),
	SubReduceDamagePercentWind(217),
	SubReduceDamagePercentFire(218),
	SubReduceDamagePercentNeutral(219),
	SubReduceDamagePercentPvPWater(255),
	SubReduceDamagePercentPvPEarth(256),
	SubReduceDamagePercentPvPWind(257),
	SubReduceDamagePercentPvPFire(258),
	SubReduceDamagePercentPvpNeutral(259),
	SubReduceDamageWater(246),
	SubReduceDamageEarth(245),
	SubReduceDamageWind(247),
	SubReduceDamageFire(248),
	SubReduceDamageNeutral(249),

	StolenWater(91),
	StolenEarth(92),
	StolenWind(93),
	StolenFire(94),
	StolenNeutral(95),
	InflictDamageWater(96),
	InflictDamageEarth(97),
	InflictDamageWind(98),
	InflictDamageFire(99),
	InflictDamageNeutral(100),

    AddSpell(604),
    AddCharactForce(607),
    AddCharactSagesse(678),
    AddCharactChance(608),
    AddCharactAgilite(609),
    AddCharactVitalite(610),
    AddCharactIntelligence(611),
    AddCharactPoint(612),
    AddSpellPoint(613),

    SubActionPointsDodge(101),
    SubMovementPointsDodge(127),

    AddReduceDamagePhysic(183),
    AddReduceDamageMagic(184),

    PetLife(800);

    private int value;
    ItemEffectEnum(int value) {
        this.value = value;
    }
    public int value() {
        return value;
    }
    public boolean isWeaponEffect() {
    	switch (this) {
    	case StolenWater:
    	case StolenEarth:
    	case StolenWind:
    	case StolenFire:
    	case StolenNeutral:
    	case InflictDamageWater:
    	case InflictDamageEarth:
    	case InflictDamageWind:
    	case InflictDamageFire:
    	case InflictDamageNeutral:
    		return true;
    		
		default:
			return false;
    	}
    }

    private static final Map<Integer, ItemEffectEnum> values = new HashMap<Integer, ItemEffectEnum>();
    static {
        for (ItemEffectEnum e : values()) {
            values.put(e.value(), e);
        }
    }
    public static ItemEffectEnum valueOf(int value) {
        ItemEffectEnum val = values.get(value);
        return val != null ? val : NONE;
    }
    
    public CharacteristicType toCharacteristicType(AtomicReference<Boolean> add) {
    	add.set(false);
    	
    	switch (this) {
    	case AddProspection:
    		add.set(true);
    	case SubProspection:
    		return CharacteristicType.Prospection;
    		
    	case AddInitiative:
    		add.set(true);
    	case SubInitiative:
    		return CharacteristicType.Initiative;
    		
    	case AddActionPoints:
    		add.set(true);
    	case SubActionPoints:
    		return CharacteristicType.ActionPoints;
    		
    	case AddMovementPoints:
    		add.set(true);
    	case SubMovementPoints:
    		return CharacteristicType.MovementPoints;
    		
    	case AddStrength:
    		add.set(true);
    	case SubStrength:
    		return CharacteristicType.Strength;
    		
    	case AddIntelligence:
    		add.set(true);
    	case SubIntelligence:
    		return CharacteristicType.Intelligence;
    		
    	case AddChance:
    		add.set(true);
    	case SubChance:
    		return CharacteristicType.Chance;
    		
    	case AddAgility:
    		add.set(true);
    	case SubAgility:
    		return CharacteristicType.Agility;
    		
    	case AddVitality:
    		add.set(true);
    	case SubVitality:
    		return CharacteristicType.Vitality;
    		
    	case AddWisdom:
    		add.set(true);
    	case SubWisdom:
    		return CharacteristicType.Wisdom;
    		
    	case AddRangePoints:
    		add.set(true);
    	case SubRangePoints:
    		return CharacteristicType.RangePoints;
    		
    	case AddSummons:
    		add.set(true);
    		return CharacteristicType.Summons;
    		
    	case AddDamage:
    		add.set(true);
    	case SubDamage:
    		return CharacteristicType.Damage;
    		
    	case AddDamagePercent:
    		add.set(true);
    		return CharacteristicType.DamagePer;
    		
    	case AddCriticalHit:
    		add.set(true);
    	case SubCriticalHit:
    		return CharacteristicType.CriticalHit;
    		
    	case AddCriticalFailure:
    		add.set(true);
    		return CharacteristicType.CriticalFailure;
    		
    	case AddDodgeAP:
    		add.set(true);
    	case SubDodgeAP:
    		return CharacteristicType.DodgeActionPoints;
    		
    	case AddDodgeMP:
    		add.set(true);
    	case SubDodgeMP:
    		return CharacteristicType.DodgeMovementPoints;
    		
    	case AddLife:
    		add.set(true);
    		return CharacteristicType.Life;
    		
    	case AddPods:
    		add.set(true);
    	case SubPods:
    		return CharacteristicType.Pods;
    		
		case AddHealPoints:
			add.set(true);
		case SubHealPoints:
			return CharacteristicType.HealPoints;
			
		case AddRenvoiDamage:
			add.set(true);
			return CharacteristicType.DamageReturn;
    		
    	// TODO resistances
    		
		default: return null;
    	}
    }

    public SpellEffectTypeEnum toDamageSpellEffectType() {
        switch (this) {
        case StolenWater:
            return SpellEffectTypeEnum.StealWater;
        case StolenEarth:
            return SpellEffectTypeEnum.StealEarth;
        case StolenWind:
            return SpellEffectTypeEnum.StealWind;
        case StolenFire:
            return SpellEffectTypeEnum.StealFire;
        case StolenNeutral:
            return SpellEffectTypeEnum.StealNeutral;
        case InflictDamageWater:
            return SpellEffectTypeEnum.DamageWater;
        case InflictDamageEarth:
            return SpellEffectTypeEnum.DamageEarth;
        case InflictDamageWind:
            return SpellEffectTypeEnum.DamageWind;
        case InflictDamageFire:
            return SpellEffectTypeEnum.DamageFire;
        case InflictDamageNeutral:
            return SpellEffectTypeEnum.DamageNeutral;

        default:
            return null;
        }
    }
}
