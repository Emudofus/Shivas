package org.shivas.server.services;

import org.shivas.common.services.IoSessionHandler;
import org.shivas.server.core.logging.DofusLogger;

public interface BaseHandler extends IoSessionHandler<String> {
	
	DofusLogger tchat();
	DofusLogger console();
	
}
