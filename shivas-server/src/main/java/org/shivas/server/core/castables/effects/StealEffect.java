package org.shivas.server.core.castables.effects;

import org.shivas.common.random.Dice;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.server.core.fights.Fight;
import org.shivas.server.core.fights.FightCell;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.Fighter;
import org.shivas.server.core.fights.events.FighterLifeUpdateEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 22:23
 */
public class StealEffect extends Effect {
    public static EffectProvider provider(final SpellEffectTypeEnum spellEffect) {
        return new EffectProvider() {
            public EffectInterface provide(SpellLevel level) {
                return new StealEffect(level, spellEffect);
            }
        };
    }

    private Dice dice;

    protected StealEffect(SpellLevel spellLevel, SpellEffectTypeEnum spellEffect) {
        super(spellLevel, spellEffect);
    }

    @Override
    public void setValue1(int value1) { }

    @Override
    public void setValue2(int value2) { }

    @Override
    public void setValue3(int value3) { }

    @Override
    public void setChance(int chance) { }

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

        int damage = DamageEffect.computeDamage(dice, caster.getStats(), spellEffect);
        int resistance = DamageEffect.computeResistance(target.getStats(), spellEffect);

        int delta1 = target.getStats().life().minus(damage - resistance);
        int delta2 = caster.getStats().life().plus(delta1 / 2);

        fight.getEvent().publish(new FighterLifeUpdateEvent(caster, target, -delta1));
        fight.getEvent().publish(new FighterLifeUpdateEvent(caster, caster, delta2));
    }

    @Override
    public EffectInterface copy() {
        return new StealEffect(spellLevel, spellEffect) {{
            setDice(dice.copy());
        }};
    }
}
