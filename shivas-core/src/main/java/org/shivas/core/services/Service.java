package org.shivas.core.services;

import org.shivas.core.config.ConfigProvider;
import org.shivas.core.database.RepositoryContainer;

public interface Service<C extends Client<?>> {
	
	void start();
	void stop();
	
	void addListener(ServiceListener<C> listener);	
	
	ConfigProvider config();
	RepositoryContainer repositories();
	
}
