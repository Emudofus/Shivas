package org.shivas.server.config;

public class DefaultConfig implements Config {

	public int getLoginPort() {
		return 5555;
	}

	public int getGamePort() {
		return 5556;
	}

}
