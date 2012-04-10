package org.shivas.game.services;

import org.shivas.game.configuration.GameConfig;

public interface LoginService {
	void start();
	void stop();
	
	GameConfig getConfig();
}
