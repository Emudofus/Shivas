package org.shivas.server.services.game;

import org.shivas.server.core.actions.ActionList;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.BaseHandler;

public final class DefaultGameClient implements GameClient {
	
	private final GameService service;
	
	private Account account;
	private Player player;
	private BaseHandler handler;
	private ActionList actions = new ActionList(this);

	public DefaultGameClient(GameService service) {
		this.service = service;
	}

	public GameService service() {
		return service;
	}

	public void kick() {
		handler.kick();
	}
	
	public BaseHandler handler() {
		return handler;
	}

	public void newHandler(BaseHandler handler) throws Exception {
		this.handler = handler;
		this.handler.init();
	}

	public Account account() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Player player() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ActionList actions() {
		return actions;
	}

}
