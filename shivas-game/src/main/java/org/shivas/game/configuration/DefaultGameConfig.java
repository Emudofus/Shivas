package org.shivas.game.configuration;

public class DefaultGameConfig implements GameConfig {

	public int getPort() {
		return 5555;
	}

	public int getSystemPort() {
		return 4444;
	}

}
