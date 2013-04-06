package org.shivas.protocol.client;

/**
 * @author Blackrush
 */
public interface Parsable {
    String getHeader();
    Object parse(String input);
}
