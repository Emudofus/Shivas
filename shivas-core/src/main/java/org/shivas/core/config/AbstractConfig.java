package org.shivas.core.config;

import org.joda.time.Duration;
import org.shivas.core.core.maps.GameMap;
import org.shivas.data.Containers;
import org.shivas.protocol.client.enums.FightTypeEnum;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/11/12
 * Time: 19:03
 */
public abstract class AbstractConfig implements Config {
    protected String databaseConnection;
    protected String databaseUser;
    protected String databasePassword;
    protected int databaseFlushDelay;
    protected int databaseSaveDelay;
    protected int databaseRefreshRate;

    protected String dataPath;
    protected String dataExtension;

    protected String modPath;

    protected int loginPort;

    protected int gameId;
    protected String gameAddress;
    protected int gamePort;

    protected String clientVersion;
    protected String motd;
    protected String cmdPrefix;
    protected boolean cmdEnabled;

    protected int maxPlayersPerAccount;
    protected short deleteAnswerLevelNeeded;
    protected short maxSpellLevel;
    protected boolean addAllWaypoints;
    protected int npcBuyCoefficient;

    protected short startSize;
    protected short startLevel;
    protected long startKamas;
    protected int startMapId;
    protected GameMap startMap;
    protected short startCell;

    protected Short startActionPoints;
    protected Short startMovementPoints;
    protected short startVitality;
    protected short startWisdom;
    protected short startStrength;
    protected short startIntelligence;
    protected short startChance;
    protected short startAgility;

    protected String infoName;
    protected String infoColor;
    protected String errorName;
    protected String errorColor;
    protected String warnName;
    protected String warnColor;

    protected int fightWorkersNum;
    protected Map<FightTypeEnum, Duration> turnDuration;

    @Override public String databaseConnection() { return databaseConnection; }
    @Override public String databaseUser() { return databaseUser; }
    @Override public String databasePassword() { return databasePassword; }
    @Override public int databaseFlushDelay() { return databaseFlushDelay; }
    @Override public int databaseSaveDelay() { return databaseSaveDelay; }
    @Override public int databaseRefreshRate() { return databaseRefreshRate; }

    @Override public String dataPath() { return dataPath; }
    @Override public String dataExtension() { return dataExtension; }

    @Override public String modPath() { return modPath; }

    @Override public int loginPort() { return loginPort; }

    @Override public int gameId() { return gameId; }
    @Override public String gameAddress() { return gameAddress; }
    @Override public int gamePort() { return gamePort; }

    @Override public String clientVersion() { return clientVersion; }
    @Override public String motd() { return motd; }
    @Override public String cmdPrefix() { return cmdPrefix; }
    @Override public boolean cmdEnabled() { return cmdEnabled; }

    @Override public int maxPlayersPerAccount() { return maxPlayersPerAccount; }
    @Override public short deleteAnswerLevelNeeded() { return deleteAnswerLevelNeeded; }
    @Override public short maxSpellLevel() { return maxSpellLevel; }
    @Override public boolean addAllWaypoints() { return addAllWaypoints; }
    @Override public int npcBuyCoefficient() { return npcBuyCoefficient; }

    @Override public short startSize() { return startSize; }
    @Override public short startLevel() { return startLevel; }
    @Override public long startKamas() { return startKamas; }
    @Override public int startMapId() { return startMapId; }
    @Override public GameMap startMap() {
        if (startMap == null) {
            startMap = Containers.instance().get(GameMap.class).byId(startMapId);
        }
        return startMap;
    }
    @Override public short startCell() { return startCell; }

    @Override public Short startActionPoints() { return startActionPoints; }
    @Override public Short startMovementPoints() { return startMovementPoints; }
    @Override public short startVitality() { return startVitality; }
    @Override public short startWisdom() { return startWisdom; }
    @Override public short startStrength() { return startStrength; }
    @Override public short startIntelligence() { return startIntelligence; }
    @Override public short startChance() { return startChance; }
    @Override public short startAgility() { return startAgility; }

    @Override public String infoName() { return infoName; }
    @Override public String infoColor() { return infoColor; }
    @Override public String errorName() { return errorName; }
    @Override public String errorColor() { return errorColor; }
    @Override public String warnName() { return warnName; }
    @Override public String warnColor() { return warnColor; }

    @Override public int fightWorkersNum() { return fightWorkersNum; }
    @Override public Duration turnDuration(FightTypeEnum fightTypeEnum) { return turnDuration.get(fightTypeEnum); }
}
