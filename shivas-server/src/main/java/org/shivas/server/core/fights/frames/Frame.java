package org.shivas.server.core.fights.frames;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.shivas.server.core.fights.Fight;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.FightTurn;

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
    private final FightTurn turn;

    protected final Fight fight;

    protected Frame(Fight fight, FightTurn turn) {
        this.fight = fight;
        this.turn = turn;
    }

    protected abstract void doBegin() throws FightException;
    protected abstract void doEnd();

    public final ListenableFuture<Frame> getEndFuture() {
        return endFuture;
    }

    public final void setNext(Frame next) {
        if (next == this) return; // avoid potentials stack overflow

        if (this.next != null) {
            this.next.setNext(next);
        } else {
            this.next = next;
        }
    }

    /**
     * begin frame only if turn is the current
     * @throws FightException
     */
    public final void begin() throws FightException {
        if (fight.getTurns().getCurrent() == turn) {
            doBegin();
        }
    }

    protected final void end() {
        doEnd();
        endFuture.set(this);

        try {
            next.begin();
        } catch (FightException e) {
            fight.exceptionThrowed(e);
        }
    }

    protected final void scheduleEnd(long millis) {
        fight.getTimer().schedule(new Runnable() {
            public void run() {
                end();
            }
        }, millis, TimeUnit.MILLISECONDS);
    }
}
