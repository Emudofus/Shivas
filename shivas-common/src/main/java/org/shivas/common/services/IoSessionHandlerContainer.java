package org.shivas.common.services;

import java.util.List;

import com.google.common.collect.Lists;

public abstract class IoSessionHandlerContainer<M> implements IoSessionHandler<M> {
	
	private List<IoSessionHandler<M>> handlers = Lists.newArrayList();
	
	protected void add(IoSessionHandler<M> handler) {
		handlers.add(handler);
	}
	
	public void init() throws Exception {
		for (IoSessionHandler<M> handler : handlers) {
			handler.init();
		}
	}

	public void handle(M message) throws Exception {
		for (IoSessionHandler<M> handler : handlers) {
			handler.handle(message);
		}
	}

	public void onClosed() {
		for (IoSessionHandler<M> handler : handlers) {
			handler.onClosed();
		}
	}

}
