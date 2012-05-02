package org.shivas.server;

import org.atomium.EntityManager;
import org.shivas.data.Container;
import org.shivas.server.config.Config;
import org.shivas.server.config.DefaultConfig;
import org.shivas.server.database.DefaultRepositoryContainer;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.database.ShivasEntityManager;
import org.shivas.server.services.game.DefaultGameService;
import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.DefaultLoginService;
import org.shivas.server.services.login.LoginService;
import org.shivas.server.utils.ContainerProvider;

import com.google.inject.AbstractModule;

public class ShivasModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Config.class).to(DefaultConfig.class);
		bind(RepositoryContainer.class).to(DefaultRepositoryContainer.class);
		bind(LoginService.class).to(DefaultLoginService.class);
		bind(GameService.class).to(DefaultGameService.class);
		bind(Container.class).toProvider(ContainerProvider.class);
		bind(EntityManager.class).to(ShivasEntityManager.class);
	}

}
