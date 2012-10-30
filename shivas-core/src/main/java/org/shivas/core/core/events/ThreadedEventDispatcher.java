package org.shivas.core.core.events;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedEventDispatcher implements EventDispatcher {
	
	private ExecutorService worker;
	
	private final List<EventListener> listeners = Lists.newArrayList();

    public ThreadedEventDispatcher() {
        this(Executors.newSingleThreadExecutor());
    }

    public ThreadedEventDispatcher(ExecutorService worker) {
        this.worker = worker;
    }

    @Override
	public void publish(final Event event) {
		worker.execute(new Runnable() {
			public void run() {
				for (EventListener listener : listeners) {
					listener.listen(event);
				}
			}
		});
	}

	@Override
	public void subscribe(final EventListener listener) {
		worker.execute(new Runnable() {
			public void run() {
				listeners.add(listener);
			}
		});
	}

	@Override
	public void unsubscribe(final EventListener listener) {
		worker.execute(new Runnable() {
			public void run() {
				listeners.remove(listener);
			}
		});
	}

	@Override
	public void clear() {
		worker.execute(new Runnable() {
			public void run() {
				listeners.clear();
			}
		});
	}

}
