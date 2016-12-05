package org.shivas.core.core.castables.effects;

import org.junit.Test;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.Fighter;
import org.shivas.core.core.fights.events.FighterLifeUpdateEvent;
import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.shivas.common.statistics.CharacteristicType.*;
import static org.shivas.common.statistics.CharacteristicType.Intelligence;
import static org.shivas.common.statistics.CharacteristicType.ResistanceFire;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public class StealEffectTest extends FightCase {
    @Test
    public void apply() throws Exception {
        StealEffect effect = new StealEffect(new SpellLevel(), SpellEffectTypeEnum.StealFire);
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

        when(target.getStats().life().minus(15)).thenReturn(15);
        when(caster.getStats().life().plus(8)).thenReturn(8);

        effect.apply(fight, caster, randomCell(target));

        mocks.verify(target.getStats().life()).minus(15);
        mocks.verify(caster.getStats().life()).plus(8);
        mocks.verify(fight.getEvent()).publish(any(FighterLifeUpdateEvent.class));
        mocks.verify(fight.getEvent()).publish(any(FighterLifeUpdateEvent.class));
    }

}