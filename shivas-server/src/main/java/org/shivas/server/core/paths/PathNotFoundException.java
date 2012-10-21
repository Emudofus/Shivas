package org.shivas.server.core.paths;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 20/10/12
 * Time: 19:14
 */
public class PathNotFoundException extends Exception {
    public PathNotFoundException() {
    }

    public PathNotFoundException(String message) {
        super(message);
    }

    public PathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathNotFoundException(Throwable cause) {
        super(cause);
    }

    public PathNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
