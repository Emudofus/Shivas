package org.shivas.core.services;

import org.shivas.common.services.IoSessionHandler;
import org.shivas.core.core.logging.DofusLogger;

public interface BaseHandler extends IoSessionHandler<String> {
	
	DofusLogger tchat();
	DofusLogger console();
	
}
