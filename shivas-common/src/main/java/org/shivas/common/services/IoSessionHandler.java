package org.shivas.common.services;

public interface IoSessionHandler<M> {
	void init() throws Exception;
	
	void handle(M message) throws Exception;
	
	void onClosed();
}
