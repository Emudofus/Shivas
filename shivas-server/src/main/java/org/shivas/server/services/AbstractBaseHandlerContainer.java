package org.shivas.server.services;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.google.common.collect.Maps;

public abstract class AbstractBaseHandlerContainer<C extends Client<?>>
	extends AbstractBaseHandler<C>
	implements BaseHandler
{
	
	private Map<Character, BaseHandler> handlers = Maps.newHashMap();
	private boolean configured;

	public AbstractBaseHandlerContainer(C client, IoSession session) {
		super(client, session);
	}
	
	protected void add(Character prefix, BaseHandler handler) {
		handlers.put(prefix, handler);
	}
	
	protected abstract void configure();
	
	protected abstract void onReceivedUnknownMessage(String message);

	public void init() throws Exception {
		if (!configured) {
			configure();
			configured = true;
		}
		
		for (BaseHandler handler : handlers.values()) {
			handler.init();
		}
	}

	public void handle(String message) throws Exception {
		BaseHandler handler = handlers.get(message.charAt(0));
		
		if (handler != null) {
			handler.handle(message);
		} else {
			onReceivedUnknownMessage(message);
		}
	}
	
	public void onClosed() {
		for (BaseHandler handler : handlers.values()) {
			handler.onClosed();
		}
	}

}
