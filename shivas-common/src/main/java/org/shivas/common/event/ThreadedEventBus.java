package org.shivas.common.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.shivas.common.threads.Future;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Blackrush
 */
public class ThreadedEventBus extends EventBus {
    public static ThreadedEventBus create(Executor executor, EventHandlerManager manager) {
        return new ThreadedEventBus(executor, manager);
    }

    public static ThreadedEventBus create(EventHandlerManager manager) {
        return create(Executors.newSingleThreadExecutor(), manager);
    }

    private final Multimap<Class<?>, Object> handlers = HashMultimap.create();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Executor executor;
    private final EventHandlerManager manager;

    private ThreadedEventBus(Executor executor, EventHandlerManager manager) {
        this.executor = executor;
        this.manager = manager;
    }

    @Override
    public void dispatch(final EventInterface event) {
        query(event);
    }

    @Override
    public Future<Object> query(final EventInterface event) {
        checkNotNull(event);

        final Future<Object> future = Future.create();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                lock.readLock().lock();
                try {
                    manager.dispatch(handlers, event, future);
                } finally {
                    lock.readLock().unlock();
                }
            }
        });
        return future;
    }

    @Override
    public void subscribe(Object subscriber) {
        checkNotNull(subscriber);

        lock.writeLock().lock();
        try {
            if (handlers.put(subscriber.getClass(), subscriber)) {
                manager.onRegistered(subscriber.getClass());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void unsubscribe(Object subscriber) {
        checkNotNull(subscriber);

        lock.writeLock().lock();
        try {
            if (handlers.remove(subscriber.getClass(), subscriber)) {
                manager.onUnregistered(subscriber.getClass());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
