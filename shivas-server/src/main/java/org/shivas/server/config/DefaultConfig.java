package org.shivas.server.config;

import org.joda.time.Duration;
import org.shivas.data.Containers;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.server.core.maps.GameMap;

import javax.inject.Singleton;

@Singleton
public class DefaultConfig implements Config {

	public String databaseConnection() {
		String hostname = "localhost", database = "shivas";
		return "jdbc:mysql://" + hostname + ":3306/" + database + "?zeroDateTimeBehavior=convertToNull";
	}

	public String databaseUser() {
		return "root";
	}

	public String databasePassword() {
		return "";
	}

	public int databaseFlushDelay() {
		return 1000 * 30; // milliseconds
	}

	@Override
	public int databaseSaveDelay() {
		return 45; // seconds
	}
	
	public int databaseRefreshRate() {
		return 1 * 60; // seconds
	}

	public String dataPath() {
		boolean linux = false; // juste pour me faciliter la vie
		if (linux) {
			return "/home/blackrush/Workspace/Shivas/data/";
		} else {
			return "D:\\Workspace\\Shivas\\data\\";
		}
	}

	public String dataExtension() {
		return "xml";
	}
	
	public String pluginPath() {
		return "D:\\Workspace\\Shivas\\plugins\\";
	}
	
	public String[] pluginExtensions() {
		return new String[] {
				"groovy",
				"rb"
		};
	}

	public int loginPort() {
		return 5555;
	}

	public int gameId() {
		return 1; // JIVA
	}

	public String gameAddress() {
		return "127.0.0.1";
	}

	public int gamePort() {
		return 5556;
	}

	public String clientVersion() {
		return "1.29.1";
	}

	@Override
	public String motd() {
		return "Bienvenue sur la version INDEV de Shivas.";
	}

	@Override
	public String cmdPrefix() {
		return "!";
	}
	
	@Override
	public boolean cmdEnabled() {
		return true;
	}

	public int maxPlayersPerAccount() {
		return 2;
	}

	@Override
	public short maxSpellLevel() {
		return 6;
	}

	@Override
	public boolean addAllWaypoints() {
		return true;
	}

    @Override
    public int npcBuyCoefficient() {
        return 10;
    }

    @Override
	public short startSize() {
		return 100;
	}

	public short startLevel() {
		return 200;
	}

	@Override
	public long startKamas() {
		return 1000000L;
	}

	public short deleteAnswerLevelNeeded() {
		return 20;
	}
	
	@Override
	public int startMapId() {
		return 7411;
	}

	@Override
	public GameMap startMap() {
		return Containers.instance().get(GameMap.class).byId(startMapId());
	}

	@Override
	public short startCell() {
		return 355;
	}

	@Override
	public Short startActionPoints() {
		return null;
	}

	@Override
	public Short startMovementPoints() {
		return null;
	}

	@Override
	public short startVitality() {
		return 0;
	}

	@Override
	public short startWisdom() {
		return 0;
	}

	@Override
	public short startStrength() {
		return 0;
	}

	@Override
	public short startIntelligence() {
		return 0;
	}

	@Override
	public short startChance() {
		return 0;
	}

	@Override
	public short startAgility() {
		return 0;
	}

	@Override
	public String infoName() {
		return "Informations";
	}

	@Override
	public String infoColor() {
		return "#009900";
	}

	@Override
	public String errorName() {
		return "Erreur";
	}

	@Override
	public String errorColor() {
		return "#C10000";
	}

	@Override
	public String warnName() {
		return "Avertissement";
	}

	@Override
	public String warnColor() {
		return "#FF8000";
	}

    @Override
    public Duration turnDuration(FightTypeEnum fightType) {
        return Duration.standardSeconds(30);
    }

}
