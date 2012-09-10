package org.shivas.server.core.events;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Lists;

public abstract class AbstractEventDispatcher implements EventDispatcher {
	
	protected List<EventListener> listeners = Lists.newArrayList();
	protected ReentrantLock lock = new ReentrantLock();

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
		listeners.clear();
	}
}
