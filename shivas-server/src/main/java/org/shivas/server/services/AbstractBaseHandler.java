package org.shivas.server.services;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;

public abstract class AbstractBaseHandler<C extends Client<?>>
	implements IoSessionHandler<String> 
{
	
	protected final C client;
	protected final IoSession session;
	
	public AbstractBaseHandler(C client, IoSession session) {
		this.client = client;
		this.session = session;
	}
	
}
