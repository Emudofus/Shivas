package org.shivas.login.services;

import org.shivas.login.database.RepositoryContainer;

public interface GameService {
	void start();
	void stop();
	
	RepositoryContainer getRepositories();
}
