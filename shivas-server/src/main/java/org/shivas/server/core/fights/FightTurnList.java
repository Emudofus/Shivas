package org.shivas.server.core.fights;

import com.google.common.collect.Lists;
import org.shivas.protocol.client.enums.FightStateEnum;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 20:26
 */
public class FightTurnList implements Iterable<FightTurn> {
    private final List<FightTurn> turns = Lists.newArrayList();
    private final Fight fight;
    private final long rate;

    private FightTurn current;

    public FightTurnList(Fight fight, long rate) {
        this.fight = fight;
        this.rate = rate;
    }

    private class DoNextCallback implements Callable<Void> {
        public Void call() throws Exception {
            next();
            return null;
        }
    }

    private void next() throws FightException {
        if (fight.getState() != FightStateEnum.ACTIVE) return;

        turns.add(current);
        current = turns.remove(0);

        beginCurrentTurn();
    }

    private void beginCurrentTurn() throws FightException {
        fight.getTimer().schedule(new DoNextCallback(), rate, TimeUnit.SECONDS);
        current.begin();
    }

    public void begin() throws FightException {
        current = turns.remove(0);

        beginCurrentTurn();
    }

    public void end() throws FightException {

    }

    @Override
    public Iterator<FightTurn> iterator() {
        return turns.iterator();
    }
}
