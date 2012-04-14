package org.shivas.game.services;

import org.shivas.common.Account;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.game.database.models.Player;

public class DefaultGameClient implements GameClient {

	private final GameService service;
	
	private Account account;
	private Player player;
	private IoSessionHandler<String> handler;
	
	public DefaultGameClient(GameService service) {
		this.service = service;
	}

	public GameService service() {
		return service;
	}
	
	public Account account() {
		return account;
	}
	
	public void account(Account account) {
		this.account = account;
	}
	
	public Player player() {
		return player;
	}
	
	public void player(Player player) {
		this.player = player;
	}
	
	public IoSessionHandler<String> handler() {
		return handler;
	}

	public void newHandler(IoSessionHandler<String> handler) throws Exception {
		this.handler = handler;
		this.handler.init();
	}
	
}
