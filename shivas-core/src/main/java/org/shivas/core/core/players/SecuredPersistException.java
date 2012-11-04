package org.shivas.core.core.players;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 04/11/12
 * Time: 14:13
 */
public class SecuredPersistException extends Exception {
    public static enum Reason {
        FULL_ACCOUNT,
        NAME_ALREADY_EXISTS
    }

    private final Reason reason;

    public SecuredPersistException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
