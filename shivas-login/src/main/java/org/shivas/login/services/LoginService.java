package org.shivas.login.services;

import org.shivas.login.config.LoginConfig;

public interface LoginService {
	void start();
	void stop();
	
	LoginConfig getConfig();
}
