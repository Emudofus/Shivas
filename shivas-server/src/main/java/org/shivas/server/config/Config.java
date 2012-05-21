package org.shivas.server.config;

import org.shivas.server.core.maps.GMap;

public interface Config {
	
	String databaseConnection();
	String databaseUser();
	String databasePassword();
	int databaseFlushDelay();
	int databaseSaveDelay();
	
	String dataPath();
	String dataExtension();
	
	int loginPort();

	int gameId();
	String gameAddress();
	int gamePort();
	
	String clientVersion();
	String motd();
	String cmdPrefix();
	
	int maxPlayersPerAccount();
	short deleteAnswerLevelNeeded();
	
	short startLevel();
	GMap startMap();
	short startCell();
	
	Short startActionPoints();
	Short startMovementPoints();
	short startVitality();
	short startWisdom();
	short startStrength();
	short startIntelligence();
	short startChance();
	short startAgility();
	
	String infoName();
	String infoColor();
	String errorName();
	String errorColor();
	String warnName();
	String warnColor();
	
}
