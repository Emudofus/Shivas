package org.shivas.server.core.fights;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.shivas.server.core.fights.events.FightTurnEvent;
import org.shivas.server.core.fights.frames.Frame;

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

    public void begin() {
        if (current) return;

        end = DateTime.now().plus(fight.getConfig().turnDuration(fight.getFightType()));

        fight.getEvent().publish(new FightTurnEvent(FightTurnEvent.Type.START, fight, this));

        current = true;
    }

    protected void doEnd() {
        if (!current) return;

        ++past;
        fighter.getStats().resetContext();

        fight.getEvent().publish(new FightTurnEvent(FightTurnEvent.Type.STOP, fight, this));

        current = false;

        fight.getTurns().beginNextTurn();
    }

    public void end() {
        Frame frame = fight.getCurrentFrame();
        if (frame == null) {
            doEnd();
        } else {
            frame.blockNext();
            frame.getEndFuture().addListener(new Runnable() {
                public void run() {
                    doEnd();
                }
            }, fight.getWorker());
        }
    }
}
