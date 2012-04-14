package org.shivas.game.services;

import org.shivas.common.Account;
import org.shivas.protocol.client.enums.WorldStateEnum;

public interface LoginService {
	void start();
	void stop();
	
	Account getAccount(String ticket);
	void deconnection(Account account);
	void updateStatus(WorldStateEnum status);
}
