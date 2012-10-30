package org.shivas.core.core.fights;

import org.shivas.core.core.interactions.InteractionException;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:38
 */
public class FightException extends InteractionException {
    public FightException() {
    }

    public FightException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public FightException(String arg0) {
        super(arg0);
    }

    public FightException(Throwable arg0) {
        super(arg0);
    }
}
