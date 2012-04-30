package org.shivas.server.services;

import org.apache.mina.core.session.IoSession;

public abstract class AbstractBaseHandler<C extends Client<?>>
	implements BaseHandler
{
	
	protected final C client;
	protected final IoSession session;
	
	public AbstractBaseHandler(C client, IoSession session) {
		this.client = client;
		this.session = session;
	}
	
	public void kick() {
		session.close(false);
	}
	
	public boolean isLoopback() {
		return session.getRemoteAddress().toString().contains("127.0.0.1"); // TODO
	}
	
	protected void assertTrue(boolean b, String message, Object... obj) throws Exception {
		if (!b) throw new Exception(String.format(message, obj));
	}
	
}
