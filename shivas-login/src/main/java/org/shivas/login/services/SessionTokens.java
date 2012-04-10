package org.shivas.login.services;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;

public class SessionTokens {
	private SessionTokens() {}
	
	public static final String HANDLER = "handler";
	public static final String TICKET  = "ticket";
	
	@SuppressWarnings("unchecked")
	public static IoSessionHandler<String> handler(IoSession session) {
		return (IoSessionHandler<String>) session.getAttribute(HANDLER);
	}
	
	public static IoSessionHandler<String> handler(IoSession session, IoSessionHandler<String> handler) {
		session.setAttribute(HANDLER, handler);
		return handler;
	}
	
	public static String ticket(IoSession session) {
		return (String) session.getAttribute(TICKET);
	}
	
	public static String ticket(IoSession session, String ticket) {
		session.setAttribute(TICKET, ticket);
		return ticket;
	}
}
