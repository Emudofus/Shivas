package org.shivas.core.core.castables.effects;

import org.shivas.common.random.Dice;
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
 * Time: 22:23
 */
public class StealEffect extends Effect {
    private Dice dice;

    public StealEffect(SpellLevel spellLevel, SpellEffectTypeEnum spellEffect) {
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

        int damage = DamageEffect.computeDamage(dice, caster.getStats(), spellEffect);
        int resistance = DamageEffect.computeResistance(target.getStats(), spellEffect);

        int delta1 = target.getStats().life().minus(damage - resistance);
        int delta2 = caster.getStats().life().plus(delta1 / 2);

        fight.getEvent().publish(new FighterLifeUpdateEvent(caster, target, -delta1));
        if (delta2 > 0) fight.getEvent().publish(new FighterLifeUpdateEvent(caster, caster, delta2));
    }

    @Override
    protected StealEffect emptyCopy() {
        return new StealEffect(spellLevel, spellEffect);
    }
}
