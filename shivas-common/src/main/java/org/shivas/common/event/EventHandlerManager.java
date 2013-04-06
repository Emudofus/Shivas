package org.shivas.common.event;

import com.google.common.collect.Multimap;
import org.shivas.common.threads.Future;

/**
 * @author Blackrush
 *         Mambo
 */
public abstract class EventHandlerManager {
    public abstract void onRegistered(Class<?> handlerClass);
    public abstract void onUnregistered(Class<?> handlerClass);

    public abstract void dispatch(Multimap<Class<?>, Object> handlers, EventInterface event, Future<Object> future);

    public EventHandlerManager then(final EventHandlerManager other) {
        final EventHandlerManager that = this;
        return new EventHandlerManager() {
            @Override
            public void onRegistered(Class<?> handlerClass) {
                that.onRegistered(handlerClass);
                other.onRegistered(handlerClass);
            }

            @Override
            public void onUnregistered(Class<?> handlerClass) {
                that.onUnregistered(handlerClass);
                other.onUnregistered(handlerClass);
            }

            @Override
            public void dispatch(Multimap<Class<?>, Object> handlers, EventInterface event, Future<Object> future) {
                that.dispatch(handlers, event, future);
                other.dispatch(handlers, event, future);
            }
        };
    }
}
