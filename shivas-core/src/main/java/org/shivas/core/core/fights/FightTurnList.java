package org.shivas.core.core.fights;

import com.google.common.base.Function;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.core.utils.Converters;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.shivas.common.collections.CollectionQuery.from;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 20:26
 */
public class FightTurnList implements Iterable<FightTurn> {
    private final List<FightTurn> turns;
    private final Fight fight;

    private FightTurn current;

    public FightTurnList(Fight fight) {
        this.fight = fight;

        turns = from(fight.getChallengers())
               .with(fight.getDefenders())
               .orderBy(Fighter.compareBy(CharacteristicType.Initiative))
               .transform(new Function<Fighter, FightTurn>() {
                    public FightTurn apply(Fighter input) {
                        return new FightTurn(FightTurnList.this.fight, input);
                    }
               })
               .computeList();

        current = turns.get(0);
    }

    public void beginNextTurn() {
        fight.purgeScheduledTasks();
        fight.eraseCurrentFrame();

        next().begin();

        fight.schedule(current.getRemaining(), new Runnable() {
            public void run() {
                current.end();
            }
        });
    }

    public FightTurn getCurrent() {
        return current;
    }

    public FightTurn next() {
        turns.remove(0);
        turns.add(current);

        return (current = turns.get(0));
    }

    @Override
    public Iterator<FightTurn> iterator() {
        return turns.iterator();
    }

    public Collection<Integer> toInt() {
        return from(turns).transform(Converters.TURN_TO_FIGHTER_ID).lazyCollection();
    }
}
