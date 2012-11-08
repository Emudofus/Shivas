package org.shivas.core.config;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/11/12
 * Time: 20:29
 */
public class UnknownKeyException extends RuntimeException {
    public UnknownKeyException(String key) {
        super("Unknown key \"" + key + "\"");
    }
}
