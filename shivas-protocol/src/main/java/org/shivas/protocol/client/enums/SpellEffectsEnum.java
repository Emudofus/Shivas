package org.shivas.protocol.client.enums;
/**
 * User: Blackrush
 * Date: 11/12/11
 * Time: 12:49
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum SpellEffectsEnum {
    None(-1),

	Teleport(4),
	PushBack(5),
	PushFront(6),
	Transpose(8),

	StealMP(77),
	StealLife(82),
	StealAP(84),

	DamageLifeWater(85),
	DamageLifeEarth(86),
	DamageLifeWind(87),
	DamageLifeFire(88),
	DamageLifeNeutral(89),
	StealWater(91),
	StealEarth(92),
	StealWind(93),
	StealFire(94),
	StealNeutral(95),
	DamageWater(96),
	DamageEarth(97),
	DamageWind(98),
	DamageFire(99),
	DamageNeutral(100),
	AddArmor(105),
	AddArmorBis(265),

	AddReturnDamage(107),
	Heal(108),
	DamageThrower(109),
	AddLife(110),
	AddAP(111),
	AddDamage(112),
	MultiplyDamage(114),

	AddAPBis(120),
	AddAgility(119),
	AddChance(123),
	AddDamagePercent(138),
	AddDamageCritic(115),
	AddDamageTrap(225),
	AddDamageTrapPercent(225),
	AddDamagePhysic(142),
	AddDamageMagic(143),
	AddCriticalFailure(122),
	AddDodgeAP(160),
	AddDodgeMP(161),
	AddStrength(118),
	AddInitiative(174),
	AddIntelligence(126),
	AddInvocationMax(182),
	AddMP(128),
	AddPO(117),
	AddPods(158),
	AddProspection(176),
	AddWisdom(124),
	AddCarePoints(178),
	AddVitality(125),
	SubAgility(154),

	SubChance(152),
	SubDamage(164),
	SubDamageCritic(171),
	SubDamageMagic(172),
	SubDamagePhysic(173),
	SubDodgeAP(162),
	SubDodgeMP(163),
	SubStrength(157),
	SubInitiative(175),
	SubIntelligence(155),
	SubAPDodge(101),
	SubMPDodge(127),
	SubAP(168),
	SubMP(169),
	SubPO(116),
	SubPods(159),
	SubProspection(177),
	SubWisdom(156),
	SubCarePoints(179),
	SubVitality(153),

	Invocation(181),

	AddReduceDamagePhysic(183),
	AddReduceDamageMagic(184),

	AddReduceDamagePercentWater(211),
	AddReduceDamagePercentEarth(210),
	AddReduceDamagePercentWind(212),
	AddReduceDamagePercentFire(213),
	AddReduceDamagePercentNeutral(214),
	AddReduceDamagePercentPvPWater(251),
	AddReduceDamagePercentPvPEarth(250),
	AddReduceDamagePercentPvAPir(252),
	AddReduceDamagePercentPvPFire(253),
	AddReduceDamagePercentPvpNeutral(254),

	AddReduceDamageWater(241),
	AddReduceDamageEarth(240),
	AddReduceDamageWind(242),
	AddReduceDamageFire(243),
	AddReduceDamageNeutral(244),
	AddReduceDamagePvPWater(261),
	AddReduceDamagePvPEarth(260),
	AddReduceDamagePvAPir(262),
	AddReduceDamagePvPFire(263),
	AddReduceDamagePvPNeutral(264),

	SubReduceDamagePercentWater(216),
	SubReduceDamagePercentEarth(215),
	SubReduceDamagePercentWind(217),
	SubReduceDamagePercentFire(218),
	SubReduceDamagePercentNeutral(219),
	SubReduceDamagePercentPvPWater(255),
	SubReduceDamagePercentPvPEarth(256),
	SubReduceDamagePercentPvAPir(257),
	SubReduceDamagePercentPvPFire(258),
	SubReduceDamagePercentPvpNeutral(259),
	SubReduceDamageWater(246),
	SubReduceDamageEarth(245),
	SubReduceDamageWind(247),
	SubReduceDamageFire(248),
	SubReduceDamageNeutral(249),

	Carry(50),
	Launch(51),
	ChangeSkin(149),
	SpellBoost(293),
	UseTrap(400),
	UseGlyph(401),
	DoNothing(666),
	DamageLife(672),
	PushFear(783),
	AddPunishment(788),
	AddState(950),
	LostState(951),
	Invisible(150),
	ClearBuffs(132),

	AddSpell(604),
	AddCharacteristicStrength(607),
	AddCharacteristicWisdom(678),
	AddCharacteristicChance(608),
	AddCharacteristicAgility(609),
	AddCharacteristicVitality(610),
	AddCharacteristicIntelligence(611),
	AddCharacteristicPoint(612),
	AddSpellPoint(613),

	LastEat(808),

	MountOwner(995),

	LivingGfxId(970),
	LivingMood(971),
	LivingSkin(972),
	LivingType(973),
	LivingXp(974),

	CanBeExchange(983),

	Incarnation(669);

    private int value;

    private SpellEffectsEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    private static final Map<Integer, SpellEffectsEnum> values = new HashMap<Integer, SpellEffectsEnum>();

    static {
        for (SpellEffectsEnum e : values()) {
            values.put(e.value(), e);
        }
    }

    public static SpellEffectsEnum valueOf(int value) {
        return values.get(value);
    }

    public static SpellEffectsEnum fromItemEffect(ItemEffectEnum effect){
        switch (effect) {
            case StolenWater:
            case InflictDamageWater:
                return DamageWater;

            case StolenEarth:
            case InflictDamageEarth:
                return DamageEarth;

            case StolenWind:
            case InflictDamageWind:
                return DamageWind;

            case StolenFire:
            case InflictDamageFire:
                return DamageFire;

            case StolenNeutral:
            case InflictDamageNeutral:
                return DamageNeutral;

            default: return null;
        }
    }
}
