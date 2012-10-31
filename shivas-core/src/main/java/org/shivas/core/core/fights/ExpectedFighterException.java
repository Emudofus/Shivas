package org.shivas.core.core.fights;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 14:15
 */
public class ExpectedFighterException extends FightException {
    public ExpectedFighterException(short cellId) {
        super("a fighter was expected on cell " + cellId);
    }

    public ExpectedFighterException(String message) {
        super(message);
    }
}
