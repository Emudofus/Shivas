package org.shivas.server.core.fights;

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
    private boolean current, left;

    public FightTurn(Fight fight, Fighter fighter) {
        this.fight = fight;
        this.fighter = fighter;
    }

    public Fighter getFighter() {
        return fighter;
    }

    public int getPast() {
        return past;
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

    public void begin() throws FightException {
        ++past;
    }

    protected void doEnd() throws FightException {

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

    public void end() throws FightException {
        if (fighter.getCurrentAction() == null) {
            doEnd();
        } else {
            fighter.getCurrentAction().getEndFuture().addListener(new DoEndCallback(), fight.getWorker());
        }
    }
}
