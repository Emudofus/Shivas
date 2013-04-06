package org.shivas.common.event;

import org.shivas.common.threads.Future;

/**
 * @author Blackrush
 */
public abstract class EventBus {
    public abstract void dispatch(EventInterface event);
    public abstract Future<Object> query(EventInterface event);

    public abstract void subscribe(Object subscriber);
    public abstract void unsubscribe(Object subscriber);
}
