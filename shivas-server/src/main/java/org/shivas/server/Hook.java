package org.shivas.server;

import org.shivas.data.Container;
import org.shivas.server.config.Config;
import org.shivas.server.core.actions.ShivasActionFactory;
import org.shivas.server.core.fights.FightFactory;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.LoginService;

import javax.inject.Inject;

public class Hook {
    @Inject
	private Config config;

    @Inject
	private Container ctner;

    @Inject
	private LoginService login;

    @Inject
	private GameService game;

    @Inject
	private RepositoryContainer repos;

    @Inject
	private ShivasActionFactory actions;

    @Inject
    private FightFactory fightFactory;

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

    public FightFactory getFightFactory() {
        return fightFactory;
    }
}
