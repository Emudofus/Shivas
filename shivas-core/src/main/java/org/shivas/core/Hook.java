package org.shivas.core;

import org.shivas.core.config.ConfigProvider;
import org.shivas.data.Container;
import org.shivas.core.core.actions.ShivasActionFactory;
import org.shivas.core.core.fights.FightFactory;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.services.game.GameService;
import org.shivas.core.services.login.LoginService;

import javax.inject.Inject;

public class Hook {
    @Inject
	private ConfigProvider config;

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

	public ConfigProvider getConfig() {
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
