package org.shivas.game.services;

import org.apache.mina.core.session.IoSession;

public class SessionTokens {
	private SessionTokens() {}
	
	private static final String PREFIX = "game.service.";
	private static final String CLIENT_TOKEN = PREFIX + "client";
	
	@SuppressWarnings("unchecked")
	public static <T extends GameClient> T client(IoSession s) {
		return (T) s.getAttribute(CLIENT_TOKEN);
	}
	
	public static void client(IoSession s, GameClient c) {
		s.setAttribute(CLIENT_TOKEN, c);
	}
}
