package org.shivas.server.services;

import org.shivas.server.config.Config;
import org.shivas.server.database.RepositoryContainer;

public interface Service<C extends Client<?>> {
	
	void start();
	void stop();
	
	void addListener(ServiceListener<C> listener);	
	
	Config config();
	RepositoryContainer repositories();
	
}
