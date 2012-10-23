package org.shivas.server.core.fights;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.shivas.server.core.fights.events.FightTurnEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:52
 */
public class FightTurn {
    private final Fight fight;
    private final Fighter fighter;

    private int past;
    private DateTime end;
    private boolean current, left;

    public FightTurn(Fight fight, Fighter fighter) {
        this.fight = fight;
        this.fighter = fighter;
        this.fighter.setTurn(this);
    }

    public Fighter getFighter() {
        return fighter;
    }

    public int getPast() {
        return past;
    }

    public Duration getRemaining() {
        return new Duration(DateTime.now(), end);
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isLeft() {
        return left;
    }

    /**
     * should end turn
     * @param left
     * @throws FightException
     */
    public void setLeft(boolean left) throws FightException {
        if (left) return;
        this.left = left;

        if (current) {
            end();
        }
    }

    private class DoEndCallback implements Runnable {
        public void run() {
            try {
                doEnd();
            } catch (FightException e) {
                fight.exceptionThrowed(e);
            }
        }
    }

    public void begin() throws FightException {
        if (current) throw new FightException("you can not begin a running turn");

        Duration turnDuration = fight.getConfig().turnDuration(fight.getFightType());

        end = DateTime.now().plus(turnDuration);
        fight.getTimer().schedule(new DoEndCallback(), turnDuration.getMillis(), TimeUnit.MILLISECONDS);

        fight.getEvent().publish(new FightTurnEvent(FightTurnEvent.Type.START, fight, this));

        current = true;
    }

    protected void doEnd() throws FightException {
        if (!current) throw new FightException("you have to begin the turn before end it");

        ++past;

        fighter.getStats().resetContext();

        fight.eraseCurrentFrame();
        fight.getEvent().publish(new FightTurnEvent(FightTurnEvent.Type.STOP, fight, this));
        fight.getTurns().next().begin();

        current = false;
    }

    public void end() throws FightException {
        if (fight.getCurrentFrame() == null) {
            doEnd();
        } else {
            fight.getCurrentFrame().getEndFuture().addListener(new DoEndCallback(), fight.getWorker());
        }
    }
}
