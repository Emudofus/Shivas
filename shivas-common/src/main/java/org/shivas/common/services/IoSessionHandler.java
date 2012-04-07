package org.shivas.common.services;

public interface IoSessionHandler<M> {
	void handle(M message) throws Exception;
	
	void onClosed();
}
