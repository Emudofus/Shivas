package org.shivas.core.core.castables.effects;

import org.shivas.common.random.Dice;
import org.shivas.data.entity.SpellEffect;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.core.core.castables.zones.Zone;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.FightException;
import org.shivas.core.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 09:11
 */
public class SpellEffectAdapter extends SpellEffect implements EffectInterface {
    private final EffectInterface effect;

    public SpellEffectAdapter(EffectInterface effect) {
        this.effect = effect;
    }

    @Override
    public SpellLevel getSpellLevel() {
        return getLevel();
    }

    @Override
    public SpellEffectTypeEnum getSpellEffect() {
        return getType();
    }

    @Override
    public void setFirst(short first) {
        super.setFirst(first);
        effect.setValue1(first);
    }

    @Override
    public void setSecond(short second) {
        super.setSecond(second);
        effect.setValue2(second);
    }

    @Override
    public void setThird(short third) {
        super.setThird(third);
        effect.setValue3(third);
    }

    @Override
    public void setTurns(short turns) {
        super.setTurns(turns);
        effect.setNbTurns(turns);
    }

    @Override
    public void setChance(short chance) {
        super.setChance(chance);
        effect.setChance(chance);
    }

    @Override
    public void setDice(Dice dice) {
        super.setDice(dice);
        effect.setDice(dice);
    }

    @Override
    public Zone getZone() {
        return effect.getZone();
    }

    @Override
    public void setZone(Zone zone) {
        effect.setZone(zone);
    }

    @Override
    public void apply(Fight fight, Fighter caster, FightCell target) throws FightException {
        effect.apply(fight, caster, target);
    }

    @Override
    public EffectInterface copy() {
        return new SpellEffectAdapter(effect.copy());
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
    public void setNbTurns(int nbTurns) { }
}
