package org.shivas.core.core.castables.effects;

import org.shivas.core.core.fights.*;
import org.shivas.core.core.fights.events.FighterTeleportEvent;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 14:01
 */
public class TeleportEffect extends Effect {
    public TeleportEffect(SpellLevel spellLevel) {
        super(spellLevel, SpellEffectTypeEnum.Teleport);
    }

    @Override
    protected TeleportEffect emptyCopy() {
        return new TeleportEffect(spellLevel);
    }

    @Override
    public void apply(Fight fight, Fighter caster, FightCell target) throws FightException {
        if (!target.isAvailable()) {
            throw new UnavailableCellException(target);
        }

        caster.setCurrentCell(target);

        fight.getEvent().publish(new FighterTeleportEvent(caster, target));
    }
}
