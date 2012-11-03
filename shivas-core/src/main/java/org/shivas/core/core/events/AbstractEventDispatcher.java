package org.shivas.core.core.events;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractEventDispatcher implements EventDispatcher {
	
	protected List<EventListener> listeners = Lists.newArrayList();
	protected ReentrantLock lock = new ReentrantLock();

    @Override
    public void publish(Event... events) {
        publish(ImmutableSet.copyOf(events));
    }

    public void subscribe(EventListener listener) {
		lock.lock();
		listeners.add(listener);
		lock.unlock();
	}

	public void unsubscribe(EventListener listener) {
		lock.lock();
		listeners.remove(listener);
		lock.unlock();
	}
	
	public void clear() {
		lock.lock();
		listeners.clear();
		lock.unlock();
	}
}
