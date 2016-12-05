package org.shivas.core.core.castables.effects;

import org.shivas.common.random.Dice;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.FightException;
import org.shivas.core.core.fights.Fighter;
import org.shivas.core.core.fights.events.FighterLifeUpdateEvent;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 13:54
 */
public class HealEffect extends Effect {
    public static int computeRegen(Dice dice, Statistics statistics){
        return dice.roll() *
                (100 + statistics.get(CharacteristicType.Intelligence).safeTotal()) / 100 +
                statistics.get(CharacteristicType.HealPoints).safeTotal();
    }

    private Dice dice;

    public HealEffect(SpellLevel spellLevel) {
        super(spellLevel, SpellEffectTypeEnum.Heal);
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
    protected HealEffect emptyCopy() {
        return new HealEffect(spellLevel);
    }

    @Override
    public void apply(Fight fight, Fighter caster, FightCell targetCell) throws FightException {
        if (targetCell.getCurrentFighter() == null) return;

        int regen = computeRegen(dice, caster.getStats());

        Fighter target = targetCell.getCurrentFighter();
        int delta = target.getStats().life().plus(regen);

        fight.getEvent().publish(new FighterLifeUpdateEvent(caster, target, delta));
    }
}
