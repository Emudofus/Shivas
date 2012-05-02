package org.shivas.server.config;

import org.shivas.data.entity.GameMap;

public interface Config {
	
	String databaseConnection();
	String databaseUser();
	String databasePassword();
	int databaseFlushDelay();
	
	String dataPath();
	String dataExtension();
	
	int loginPort();

	int gameId();
	String gameAddress();
	int gamePort();
	
	String clientVersion();
	
	int maxPlayersPerAccount();
	short deleteAnswerLevelNeeded();
	
	short startLevel();
	GameMap startMap();
	short startCell();
	
}
