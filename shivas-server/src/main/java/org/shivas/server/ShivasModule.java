package org.shivas.server;

import org.shivas.data.*;
import org.shivas.data.entity.factory.ItemActionFactory;
import org.shivas.server.config.*;
import org.shivas.server.core.commands.CommandEngine;
import org.shivas.server.core.items.actions.ShivasItemActionFactory;
import org.shivas.server.database.*;
import org.shivas.server.services.game.*;
import org.shivas.server.services.login.*;
import org.shivas.server.utils.ContainerProvider;

import com.google.inject.AbstractModule;

public class ShivasModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Config.class).to(DefaultConfig.class).asEagerSingleton();
		
		bind(EntityFactory.class).to(ShivasEntityFactory.class).asEagerSingleton();
		bind(Container.class).toProvider(ContainerProvider.class);
		bind(ItemActionFactory.class).to(ShivasItemActionFactory.class);
		
		bind(LoginService.class).to(DefaultLoginService.class);
		bind(GameService.class).to(DefaultGameService.class);
		bind(CommandEngine.class).asEagerSingleton();
		
		install(new ShivasDatabaseModule());
	}

}
