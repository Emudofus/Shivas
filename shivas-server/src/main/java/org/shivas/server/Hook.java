package org.shivas.server;

import javax.inject.Inject;

import org.shivas.data.Container;
import org.shivas.server.config.Config;
import org.shivas.server.core.actions.ShivasActionFactory;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.LoginService;

public class Hook {
	private Config config;
	private Container ctner;
	private LoginService login;
	private GameService game;
	private RepositoryContainer repos;
	private ShivasActionFactory actions;

	@Inject
	public Hook(Config config, Container ctner, LoginService lservice, GameService gservice, RepositoryContainer repos, ShivasActionFactory actions) {
		this.config = config;
		this.ctner = ctner;
		this.login = lservice;
		this.game = gservice;
		this.repos = repos;
		this.actions = actions;
	}

	public Config getConfig() {
		return config;
	}

	public Container getContainer() {
		return ctner;
	}
	
	public LoginService getLogin() {
		return login;
	}

	public GameService getGame() {
		return game;
	}

	public RepositoryContainer getRepositories() {
		return repos;
	}
	
	public ShivasActionFactory getActionFactory() {
		return actions;
	}
}
