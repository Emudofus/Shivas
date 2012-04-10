package org.shivas.game;

import org.shivas.game.configuration.DefaultGameConfig;
import org.shivas.game.configuration.GameConfig;
import org.shivas.game.database.DefaultRepositoryContainer;
import org.shivas.game.database.RepositoryContainer;
import org.shivas.game.services.DefaultLoginService;
import org.shivas.game.services.LoginService;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class ShivasGameModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule(ShivasGameServer.UNIT_PERSIST_NAME));
		
		bind(GameConfig.class).to(DefaultGameConfig.class);
		bind(RepositoryContainer.class).to(DefaultRepositoryContainer.class);
		bind(LoginService.class).to(DefaultLoginService.class);
	}

}
