package org.shivas.login.services;

import org.shivas.common.crypto.Cipher;
import org.shivas.login.config.LoginConfig;
import org.shivas.login.database.RepositoryContainer;

public interface LoginService {
	void start();
	void stop();
	
	LoginConfig getConfig();
	RepositoryContainer getRepositories();
	GameService getGameService();
	Cipher getDecrypter(String key);
}