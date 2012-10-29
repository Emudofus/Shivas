package org.shivas.server.core.castables.effects;

import org.shivas.common.random.Dice;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.server.core.fights.Fight;
import org.shivas.server.core.fights.FightCell;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 09:23
 * TODO
 */
public class DamageEffect extends Effect {
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

        Fighter fighter = targetCell.getCurrentFighter();

        int damage = dice.roll();

        fighter.getStats().life().minus(damage);
    }

    @Override
    public DamageEffect copy() {
        return new DamageEffect(spellLevel, spellEffect);
    }
}
