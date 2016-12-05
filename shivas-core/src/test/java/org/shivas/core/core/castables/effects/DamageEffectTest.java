package org.shivas.core.core.castables.effects;

import org.junit.Test;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.Fighter;
import org.shivas.core.core.fights.events.FighterLifeUpdateEvent;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

import static org.mockito.Mockito.any;
import static org.shivas.common.statistics.CharacteristicType.*;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public class DamageEffectTest extends FightCase {
    @Test
    public void apply() throws Exception {
        DamageEffect effect = new DamageEffect(
                new SpellLevel(),
                SpellEffectTypeEnum.DamageFire
        );
        effect.setDice(new Dofus1Dice(5, 5, 5));

        Fight fight = mockFight();
        Fighter caster = mockFighter(
                stat(Intelligence, 0),
                stat(DamagePer, 0),
                stat(Damage, 0)
        );
        Fighter target = mockFighter(
                stat(ResistanceNeutral, 0),
                stat(ResistanceFire, 0),
                stat(Intelligence, 0)
        );

        effect.apply(fight, caster, randomCell(target));

        mocks.verify(target.getStats().life()).minus(15);
        mocks.verify(fight.getEvent()).publish(any(FighterLifeUpdateEvent.class));
    }

}