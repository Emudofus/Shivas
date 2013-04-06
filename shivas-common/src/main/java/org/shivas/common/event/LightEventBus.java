package org.shivas.common.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.shivas.common.threads.Future;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * a thread-unsafe {@link EventBus}
 * @author Blackrush
 */
public class LightEventBus extends EventBus {
    public static LightEventBus create(EventHandlerManager manager) {
        return new LightEventBus(manager);
    }

    private final Multimap<Class<?>, Object> handlers = HashMultimap.create();
    private final EventHandlerManager manager;

    private LightEventBus(EventHandlerManager manager) {
        this.manager = manager;
    }

    @Override
    public void dispatch(EventInterface event) {
        query(event);
    }

    @Override
    public Future<Object> query(EventInterface event) {
        Future<Object> future = Future.create();
        manager.dispatch(handlers, event, future);
        return future;
    }

    @Override
    public void subscribe(Object subscriber) {
        checkNotNull(subscriber);

        if (handlers.put(subscriber.getClass(), subscriber)) {
            manager.onRegistered(subscriber.getClass());
        }
    }

    @Override
    public void unsubscribe(Object subscriber) {
        checkNotNull(subscriber);

        if (handlers.remove(subscriber.getClass(), subscriber)) {
            manager.onUnregistered(subscriber.getClass());
        }
    }
}
