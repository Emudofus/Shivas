package org.shivas.common.observable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractObservable<O, A> implements Observable<O, A> {
	
	private List<O> observers = Collections.synchronizedList(new ArrayList<O>());
	
	protected abstract void notifyObserver(O observer, A arg);

	public void addObserver(O observer) {
		observers.add(observer);
	}

	public void removeObserver(O observer) {
		observers.remove(observer);
	}

	public void notifyObservers(A arg) {
		for (O observer : observers) {
			notifyObserver(observer, arg);
		}
	}
	
}
