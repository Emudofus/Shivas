package org.shivas.server.core.events;

import java.util.concurrent.ExecutorService;

public class ThreadedEventDispatcher extends AbstractEventDispatcher {
	
	private ExecutorService worker;

	public ThreadedEventDispatcher(ExecutorService worker) {
		this.worker = worker;
	}

	public void publish(final Event event) {
		worker.execute(new Runnable() {
			public void run() {
				lock.lock();
				for (EventListener listener : listeners) {
					listener.listen(event);
				}
				lock.unlock();
			}
		});
	}

}
