package org.shivas.common.threads;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Throwables.propagate;

/**
 * a re-usable listenable lazy value reference
 * @author Blackrush
 */
public class Future<T> {
    public static <T> Future<T> create() {
        return new Future<T>();
    }

    public static interface Listener<T> {
        void result(Future<T> future);
    }

    private final List<Listener<T>> listeners = Lists.newArrayList();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private T o;
    private Throwable error;

    private Future(){}

    /**
     * add a listener to this future
     * @param listener listener that will be notified
     */
    public void addListener(Listener<T> listener) {
        lock.writeLock().lock();
        try {
            listeners.add(listener);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void notifyListeners() {
        lock.readLock().lock();
        try {
            for (Listener<T> listener : listeners) {
                listener.result(this);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * this will notify all listeners
     * @param o value that listeners will use
     */
    public void set(T o) {
        this.o = o;
        this.error = null;
        notifyListeners();
    }

    /**
     * this will notify all listeners
     * @param error error that listeners will use
     */
    public void set(Throwable error) {
        this.error = error;
        this.o = null;
        notifyListeners();
    }

    private void waitForValue() {
        try {
            while (o == null || error == null) {
                Thread.sleep(1);
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    /**
     * @return <code>true</code> if an error has been set
     */
    public boolean hasFailed() {
        return error != null;
    }

    /**
     * @return {@link Throwable}
     */
    public Throwable getError() {
        return error;
    }

    /**
     * may throw an exception if an error has been set instead of a value or return a value
     * @return value if this future is a success
     * @throws Throwable if this future is a failure
     */
    public T get() {
        waitForValue();

        if (hasFailed()) throw propagate(error);
        else return o;
    }

    /**
     * this method is safe unlike {@code get()}
     * @return an optional reference to the value
     */
    public Optional<T> getValue() {
        return Optional.of(o);
    }
}
