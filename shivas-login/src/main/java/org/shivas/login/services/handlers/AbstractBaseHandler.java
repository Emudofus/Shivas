package org.shivas.login.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.services.LoginClient;

public abstract class AbstractBaseHandler implements IoSessionHandler<String> {

	protected final LoginClient client;
	protected final IoSession   session;
	
	public AbstractBaseHandler(LoginClient client, IoSession session) {
		this.client = client;
		this.session = session;
	}
	
}
