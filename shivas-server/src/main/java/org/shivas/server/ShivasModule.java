package org.shivas.server;

import org.atomium.EntityManager;
import org.shivas.data.*;
import org.shivas.data.entity.factory.ItemActionFactory;
import org.shivas.server.config.*;
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
		bind(ItemActionFactory.class).to(ShivasItemActionFactory.class);
		bind(EntityFactory.class).to(ShivasEntityFactory.class).asEagerSingleton();
		bind(RepositoryContainer.class).to(DefaultRepositoryContainer.class);
		bind(LoginService.class).to(DefaultLoginService.class);
		bind(GameService.class).to(DefaultGameService.class);
		bind(Container.class).toProvider(ContainerProvider.class);
		bind(EntityManager.class).to(ShivasEntityManager.class);
		install(new ShivasDatabaseModule());
	}

}
