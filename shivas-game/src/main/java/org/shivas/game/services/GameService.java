package org.shivas.game.services;

import org.shivas.game.configuration.GameConfig;
import org.shivas.game.database.RepositoryContainer;
import org.shivas.protocol.client.enums.WorldStateEnum;

public interface GameService {
	void start();
	void stop();
	
	GameConfig getConfig();
	RepositoryContainer getRepositories();
	LoginService getLoginService();
	
	WorldStateEnum getStatus();
	void setStatus(WorldStateEnum status);
}
