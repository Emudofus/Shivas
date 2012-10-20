package org.shivas.server.config;

import org.joda.time.Duration;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.server.core.maps.GameMap;

public interface Config {
	
	String databaseConnection();
	String databaseUser();
	String databasePassword();
	int databaseFlushDelay();
	int databaseSaveDelay();
	int databaseRefreshRate();
	
	String dataPath();
	String dataExtension();
	
	String pluginPath();
	String[] pluginExtensions();
	
	int loginPort();

	int gameId();
	String gameAddress();
	int gamePort();
	
	String clientVersion();
	String motd();
	String cmdPrefix();
	boolean cmdEnabled();
	
	int maxPlayersPerAccount();
	short deleteAnswerLevelNeeded();
	short maxSpellLevel();
	boolean addAllWaypoints();
    int npcBuyCoefficient();

	short startSize();
	short startLevel();
	long startKamas();
	int startMapId();
	GameMap startMap();
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

    /**
     * seconds
     * @param fightType fight type
     * @return duration
     */
    Duration turnDuration(FightTypeEnum fightType);
}
