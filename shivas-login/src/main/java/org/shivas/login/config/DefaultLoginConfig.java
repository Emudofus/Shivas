package org.shivas.login.config;

import javax.inject.Singleton;

@Singleton
public class DefaultLoginConfig implements LoginConfig {

	public int getPort() {
		return 5554;
	}

	public String getClientVersion() {
		return "1.29.1";
	}

}
