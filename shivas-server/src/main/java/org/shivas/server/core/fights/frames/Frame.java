package org.shivas.server.core.fights.frames;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.shivas.server.core.fights.Fight;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.FightTurn;
import org.shivas.server.core.fights.Fighter;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 22/10/12
 * Time: 19:32
 */
public abstract class Frame {
    private final SettableFuture<Frame> endFuture = SettableFuture.create();
    private Frame next;

    protected final FightTurn turn;
    protected final Fighter fighter;
    protected final Fight fight;

    protected Frame(FightTurn turn) {
        this.turn = turn;
        this.fighter = this.turn.getFighter();
        this.fight = this.fighter.getFight();
    }

    /**
     * @return frame's end future or next's if there is one
     */
    public final ListenableFuture<Frame> getEndFuture() {
        return next != null ? next.getEndFuture() : endFuture;
    }

    public final void setNext(Frame next) {
        if (next == this) return; // avoids stack overflow

        if (this.next != null) {
            this.next.setNext(next);
        } else {
            this.next = next;
        }
    }

    public final void blockNext() {
        this.next = null;
    }

    public abstract void begin() throws FightException;

    protected abstract void doEnd();

    protected void end() {
        endFuture.set(this);
        doEnd();

        if (next != null) {
            try {
                next.begin();
            } catch (FightException e) {
                fight.exceptionThrowed(e);
            }
        }
    }

    protected void scheduleEnd(long millis) {
        fight.getTimer().schedule(new Runnable() {
            public void run() {
                end();
            }
        }, millis, TimeUnit.MILLISECONDS);
    }
}
