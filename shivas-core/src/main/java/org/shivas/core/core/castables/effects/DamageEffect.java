package org.shivas.core.core.castables.effects;

import org.shivas.common.random.Dice;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.FightException;
import org.shivas.core.core.fights.Fighter;
import org.shivas.core.core.fights.events.FighterLifeUpdateEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 09:23
 */
public class DamageEffect extends Effect {
    public static int computeDamage(Dice dice, Statistics statistics, SpellEffectTypeEnum effect){
        int base       = dice.roll(),
            charac     = statistics.get(effect.toCharacteristicType()).safeTotal(),
            domPercent = statistics.get(CharacteristicType.DamagePer).safeTotal(),
            dom        = statistics.get(CharacteristicType.Damage).safeTotal();

        return base * (100 + charac + domPercent) / 100 + dom;
    }

    public static int computeResistance(Statistics statistics, SpellEffectTypeEnum effect){
        int resNeutral = statistics.get(CharacteristicType.ResistanceNeutral).total(),
                res = effect == SpellEffectTypeEnum.DamageNeutral ? 0 : statistics.get(effect.toResistanceCharacteristicType()).total(),
                charac = statistics.get(effect.toCharacteristicType()).safeTotal(),
                characInt = statistics.get(CharacteristicType.Intelligence).safeTotal();

        return (resNeutral + res) * Math.max(1 + charac / 100, 1 + charac / 200 + characInt / 200);
    }

    public static EffectProvider provider(final SpellEffectTypeEnum type) {
        return new EffectProvider() {
            public EffectInterface provide(SpellLevel level) {
                return new DamageEffect(level, type);
            }
        };
    }

    protected Dice dice;

    public DamageEffect(SpellLevel spellLevel, SpellEffectTypeEnum spellEffect) {
        super(spellLevel, spellEffect);
    }

    @Override
    public Dice getDice() {
        return dice;
    }

    @Override
    public void setDice(Dice dice) {
        this.dice = dice;
    }

    @Override
    public void apply(Fight fight, Fighter caster, FightCell targetCell) throws FightException {
        if (targetCell.getCurrentFighter() == null) return;

        Fighter target = targetCell.getCurrentFighter();

        int damage = computeDamage(dice, caster.getStats(), spellEffect);
        int resistance = computeResistance(target.getStats(), spellEffect);

        damage = target.getStats().life().minus(damage - resistance);

        fight.getEvent().publish(new FighterLifeUpdateEvent(caster, target, -damage));
    }

    @Override
    protected DamageEffect emptyCopy() {
        return new DamageEffect(spellLevel, spellEffect);
    }
}
