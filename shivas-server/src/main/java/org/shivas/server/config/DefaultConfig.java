package org.shivas.server.config;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.Container;
import org.shivas.data.entity.GameMap;

@Singleton
public class DefaultConfig implements Config {
	
	@Inject
	private Container ctner;

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
		return 1000 * 30; // 30 seconds
	}

	public String dataPath() {
		boolean linux = false; // juste pour me faciliter la vie
		if (linux) {
			return "/home/blackrush/Workspace/Shivas/data/";
		} else {
			return "C:\\Users\\Blackrush\\workspace\\Shivas\\data\\";
		}
	}

	public String dataExtension() {
		return "xml";
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

	public int maxPlayersPerAccount() {
		return 2;
	}

	public short startLevel() {
		return 200;
	}

	public short deleteAnswerLevelNeeded() {
		return 20;
	}

	@Override
	public GameMap startMap() {
		return ctner.get(GameMap.class).byId(7411);
	}

	@Override
	public short startCell() {
		return 355;
	}

}
