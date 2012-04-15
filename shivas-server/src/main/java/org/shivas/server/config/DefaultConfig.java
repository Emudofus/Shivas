package org.shivas.server.config;

public class DefaultConfig implements Config {

	public int loginPort() {
		return 5555;
	}

	public int gamePort() {
		return 5556;
	}

	public String clientVersion() {
		return "1.29.1";
	}

}
