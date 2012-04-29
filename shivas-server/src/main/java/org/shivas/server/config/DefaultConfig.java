package org.shivas.server.config;

import javax.inject.Singleton;

@Singleton
public class DefaultConfig implements Config {

	public String dataPath() {
		return "/home/blackrush/Workspace/Shivas/data/";
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

	public int maxPlayersPerAccount() {
		return 2;
	}

	public short startLevel() {
		return 200;
	}

	public short deleteAnswerLevelNeeded() {
		return 20;
	}

}
