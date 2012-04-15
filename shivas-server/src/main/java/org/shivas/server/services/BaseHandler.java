package org.shivas.server.services;

import org.shivas.common.services.IoSessionHandler;

public interface BaseHandler extends IoSessionHandler<String> {
	
	void kick();
	
}
