package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 12:03
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

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

	AddReduceDamagePourcentWater(211),
	AddReduceDamagePourcentEarth(210),
	AddReduceDamagePourcentWind(212),
	AddReduceDamagePourcentFire(213),
	AddReduceDamagePourcentNeutral(214),
	AddReduceDamagePourcentPvPWater(251),
	AddReduceDamagePourcentPvPEarth(250),
	AddReduceDamagePourcentPvPWind(252),
	AddReduceDamagePourcentPvPFire(253),
	AddReduceDamagePourcentPvpNeutral(254),
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

	SubReduceDamagePourcentWater(216),
	SubReduceDamagePourcentEarth(215),
	SubReduceDamagePourcentWind(217),
	SubReduceDamagePourcentFire(218),
	SubReduceDamagePourcentNeutral(219),
	SubReduceDamagePourcentPvPWater(255),
	SubReduceDamagePourcentPvPEarth(256),
	SubReduceDamagePourcentPvPWind(257),
	SubReduceDamagePourcentPvPFire(258),
	SubReduceDamagePourcentPvpNeutral(259),
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

    SubPAEsquive(101),
    SubPMEsquive(127),

    AddReduceDamagePhysic(183),
    AddReduceDamageMagic(184),

    PetLife(800);

    private int value;
    private ItemEffectEnum(int value) {
        this.value = value;
    }
    public int value() {
        return value;
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
}
