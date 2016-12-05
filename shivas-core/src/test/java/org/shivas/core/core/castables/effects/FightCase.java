package org.shivas.core.core.castables.effects;

import org.shivas.common.maths.LimitedValue;
import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.Fighter;
import org.shivas.core.core.statistics.BaseCharacteristic;
import org.shivas.data.entity.GameCell;
import org.shivas.protocol.client.enums.FightSideEnum;
import org.shivas.protocol.client.enums.FightTeamEnum;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.when;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public abstract class FightCase {
    protected final Mocks mocks = new Mocks();

    protected final Fight mockFight() {
        Fight fight = mocks.mock(Fight.class);
        when(fight.getRandom()).thenReturn(new Random(0L));
        when(fight.getEvent()).thenReturn(mocks.mock(EventDispatcher.class));
        return fight;
    }

    protected final Fighter mockFighter(Characteristic... characs) {
        Fighter fighter = mocks.mock(Fighter.class);
        Statistics stats = mocks.mock(Statistics.class);
        when(fighter.getStats()).thenReturn(stats);
        when(stats.life()).thenReturn(mocks.mock(LimitedValue.class));
        for (Characteristic charac : characs) {
            when(stats.get(charac.type())).thenReturn(charac);
        }
        return fighter;
    }

    protected final FightCell randomCell(Fighter fighter) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        FightCell cell = new FightCell(
                (short) rand.nextInt(1, 360),
                false,
                GameCell.MovementType.Walkable,
                0,
                0,
                FightSideEnum.BLUE,
                FightTeamEnum.CHALLENGERS
        );
        cell.setCurrentFighter(fighter);
        return cell;
    }

    protected final Characteristic stat(CharacteristicType type, int base) {
        return new BaseCharacteristic(type, (short) base);
    }
}
