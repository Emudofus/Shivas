package org.shivas.game.services;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;

public class SessionTokens {
	private SessionTokens() {}
	
	public static final String HANDLER = "handler";
	
	@SuppressWarnings("unchecked")
	public static IoSessionHandler<String> handler(IoSession session) {
		return (IoSessionHandler<String>) session.getAttribute(HANDLER);
	}
	
	public static IoSessionHandler<String> handler(IoSession session, IoSessionHandler<String> handler) {
		session.setAttribute(HANDLER, handler);
		return handler;
	}
}
