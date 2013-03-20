package org.shivas.core.services;

import com.typesafe.config.Config;
import org.shivas.core.database.RepositoryContainer;

public interface Service<C extends Client<?>> {
	
	void start();
	void stop();
	
	void addListener(ServiceListener<C> listener);	
	
	Config config();
	RepositoryContainer repositories();
	
}
