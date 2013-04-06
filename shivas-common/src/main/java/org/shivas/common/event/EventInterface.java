package org.shivas.common.event;

/**
 * @author Blackrush
 *         Mambo
 */
public interface EventInterface {
    /**
     * @return <code>true</code> if propagation has been stopped
     */
    boolean hasBeenStopped();

    /**
     * stop this event's propagation
     */
    void stopPropagation();

    /**
     * reply to this event's sender
     * @param message reply
     */
    void reply(Object message);
}
