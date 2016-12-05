package org.shivas.core.core.castables.effects;

import org.shivas.core.core.fights.*;
import org.shivas.core.core.fights.events.FighterTransposeEvent;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 14:13
 */
public class TransposeEffect extends Effect {
    public TransposeEffect(SpellLevel spellLevel) {
        super(spellLevel, SpellEffectTypeEnum.Transpose);
    }

    @Override
    protected TransposeEffect emptyCopy() {
        return new TransposeEffect(spellLevel);
    }

    @Override
    public void apply(Fight fight, Fighter caster, FightCell targetCell) throws FightException {
        if (targetCell.getCurrentFighter() == null) {
            throw new ExpectedFighterException(targetCell.getId());
        }

        Fighter target = targetCell.getCurrentFighter();

        caster.takePlaceOf(target);

        fight.getEvent().publish(new FighterTransposeEvent(caster, target));
    }
}
