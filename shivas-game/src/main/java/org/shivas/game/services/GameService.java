package org.shivas.game.services;

import org.shivas.game.configuration.GameConfig;
import org.shivas.game.database.RepositoryContainer;

public interface GameService {
	void start();
	void stop();
	
	GameConfig getConfig();
	RepositoryContainer getRepositories();
	LoginService getLoginService();
}
