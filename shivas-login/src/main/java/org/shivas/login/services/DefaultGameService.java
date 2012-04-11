package org.shivas.login.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.common.Account;
import org.shivas.login.database.RepositoryContainer;
import org.shivas.login.database.models.GameServer;
import org.shivas.protocol.client.types.BaseCharactersServerType;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

@Singleton
public class DefaultGameService implements GameService {
	
	@Inject
	private RepositoryContainer repositories;

	public void start() {
		for (GameServer server : repositories.getServers().findAll()) {
			GameHandler handler = new DefaultGameHandler(server, this);
			server.setGameHandler(handler);
			
			handler.start();
		}
	}

	public void stop() {
		for (GameServer server : repositories.getServers().findAll()) {
			server.getGameHandler().stop();
		}
	}
	
	public RepositoryContainer getRepositories() {
		return repositories;
	}

	public ListenableFuture<List<BaseCharactersServerType>> getNbCharactersByAccount(Account account) {
		List<ListenableFuture<BaseCharactersServerType>> futures = Lists.newArrayList();
		
		for (GameServer server : repositories.getServers().findAll()) {
			futures.add(server.getGameHandler().getNbCharacters(account));
		}
		
		return Futures.successfulAsList(futures);
	}

}
