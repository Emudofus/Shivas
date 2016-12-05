package org.shivas.core.core.castables.effects;

import org.junit.Test;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.Fighter;
import org.shivas.core.core.fights.events.FighterLifeUpdateEvent;
import org.shivas.data.entity.SpellLevel;

import static org.mockito.ArgumentMatchers.any;
import static org.shivas.common.statistics.CharacteristicType.HealPoints;
import static org.shivas.common.statistics.CharacteristicType.Intelligence;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public class HealEffectTest extends FightCase {
    @Test
    public void apply() throws Exception {
        HealEffect effect = new HealEffect(new SpellLevel());
        effect.setDice(new Dofus1Dice(5, 5, 5));

        Fight fight = mockFight();
        Fighter fighter = mockFighter(
                stat(Intelligence, 0),
                stat(HealPoints, 0)
        );

        effect.apply(fight, fighter, randomCell(fighter));

        mocks.verify(fighter.getStats().life()).plus(15);
        mocks.verify(fight.getEvent()).publish(any(FighterLifeUpdateEvent.class));
    }

}