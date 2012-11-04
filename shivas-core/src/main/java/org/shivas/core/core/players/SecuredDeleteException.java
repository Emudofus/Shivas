package org.shivas.core.core.players;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 04/11/12
 * Time: 13:58
 */
public class SecuredDeleteException extends Exception {
    public static enum Reason {
        BAD_SECRET_ANSWER,
        TOO_LOW_PLAYER_LEVEL,
    }

    private final Reason reason;

    public SecuredDeleteException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
