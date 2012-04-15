package org.shivas.server.services;

import org.shivas.common.services.IoSessionHandler;

public abstract class AbstractBaseHandler<C extends Client, S extends Service>
	implements IoSessionHandler<String> 
{
	
	protected final C client;
	protected final S service;
	
	public AbstractBaseHandler(C client, S service) {
		this.client = client;
		this.service = service;
	}
	
}
