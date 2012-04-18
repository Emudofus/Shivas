package org.shivas.common.observable;

public interface Observable<O, A> {
	void addObserver(O observer);
	void removeObserver(O observer);
	
	void notifyObservers(A arg);
}
