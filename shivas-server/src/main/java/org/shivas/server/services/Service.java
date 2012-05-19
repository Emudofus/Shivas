package org.shivas.server.services;

import org.shivas.server.config.Config;
import org.shivas.server.database.RepositoryContainer;

public interface Service {
	
	void start();
	void stop();
	
	Config config();
	RepositoryContainer repositories();
	
}
