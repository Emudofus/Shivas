package org.shivas.login.services;

import java.util.List;

import org.shivas.login.database.RepositoryContainer;
import org.shivas.login.database.models.Account;
import org.shivas.protocol.client.types.BaseCharactersServerType;

import com.google.common.util.concurrent.ListenableFuture;

public interface GameService {
	void start();
	void stop();
	
	RepositoryContainer getRepositories();
	
	ListenableFuture<List<BaseCharactersServerType>> getNbCharactersByAccount(Account account);
}
