package org.shivas.login.services;

import org.shivas.login.database.models.Account;
import org.shivas.protocol.client.enums.WorldStateEnum;
import org.shivas.protocol.client.types.BaseCharactersServerType;

import com.google.common.util.concurrent.ListenableFuture;

public interface GameHandler {
	void start();
	void stop();
	
	ListenableFuture<BaseCharactersServerType> getNbCharacters(Account account);
	
	WorldStateEnum getStatus();
}
