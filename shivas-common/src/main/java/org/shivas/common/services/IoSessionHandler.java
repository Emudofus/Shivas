package org.shivas.common.services;

public interface IoSessionHandler<M> {
	IoSessionHandler<M> init() throws Exception;
	
	void handle(M message) throws Exception;
	
	void onClosed();
}
