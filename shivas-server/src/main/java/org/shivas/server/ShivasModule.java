package org.shivas.server;

import org.shivas.common.inject.AnnotatedJpaPersistModule;
import org.shivas.server.config.Config;
import org.shivas.server.config.DefaultConfig;
import org.shivas.server.database.DefaultRepositoryContainer;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.database.annotations.DefaultDatabase;
import org.shivas.server.database.annotations.StaticDatabase;
import org.shivas.server.services.game.DefaultGameService;
import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.DefaultLoginService;
import org.shivas.server.services.login.LoginService;

import com.google.inject.AbstractModule;

public class ShivasModule extends AbstractModule {

	@Override
	protected void configure() {
		install(AnnotatedJpaPersistModule.newModule("default", DefaultDatabase.class));
		install(AnnotatedJpaPersistModule.newModule("static", StaticDatabase.class));
		
		bind(Config.class).to(DefaultConfig.class);
		bind(RepositoryContainer.class).to(DefaultRepositoryContainer.class);
		bind(LoginService.class).to(DefaultLoginService.class);
		bind(GameService.class).to(DefaultGameService.class);
	}

}
