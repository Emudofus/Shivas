package org.shivas.common.services;

import java.util.Map;

import com.google.common.collect.Maps;

public abstract class IoSessionHandlerStringContainer implements IoSessionHandler<String> {
	
	private Map<Character, IoSessionHandler<String>> handlers = Maps.newHashMap();
	
	protected abstract void configure();
	protected abstract void unknownMessage(String message);
	
	protected void add(char prefix, IoSessionHandler<String> handler) {
		handlers.put(prefix, handler);
	}

	public void init() throws Exception {
		configure();
		
		for (IoSessionHandler<String> handler : handlers.values()) {
			handler.init();
		}
	}

	public void handle(String message) throws Exception {
		IoSessionHandler<String> handler = handlers.get(message.charAt(0));
		if (handler != null) {
			handler.handle(message.substring(1));
		} else {
			unknownMessage(message);
		}
	}

	public void onClosed() {
		for (IoSessionHandler<String> handler : handlers.values()) {
			handler.onClosed();
		}
	}

}
