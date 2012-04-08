package org.shivas.login.services;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.login.database.RepositoryContainer;
import org.shivas.login.database.models.GameServer;

@Singleton
public class DefaultGameService implements GameService {
	
	@Inject
	private RepositoryContainer repositories;

	public void start() {
		for (GameServer server : repositories.getServers().findAll()) {
			GameHandler handler = new DefaultGameHandler(server, this);
			server.setHandler(handler);
			
			handler.start();
		}
	}

	public void stop() {
		for (GameServer server : repositories.getServers().findAll()) {
			server.getHandler().stop();
		}
	}
	
	public RepositoryContainer getRepositories() {
		return repositories;
	}

}
