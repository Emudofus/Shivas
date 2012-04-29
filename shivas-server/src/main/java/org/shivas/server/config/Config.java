package org.shivas.server.config;

public interface Config {
	
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
	
}
