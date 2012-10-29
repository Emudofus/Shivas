package org.shivas.server;

import org.shivas.data.*;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.server.config.*;
import org.shivas.server.core.actions.ShivasActionFactory;
import org.shivas.server.core.castables.effects.DefaultEffectFactory;
import org.shivas.server.core.castables.effects.EffectFactory;
import org.shivas.server.core.commands.CommandEngine;
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
		bind(ActionFactory.class).to(ShivasActionFactory.class);
        bind(EffectFactory.class).to(DefaultEffectFactory.class);
		
		bind(LoginService.class).to(DefaultLoginService.class);
		bind(GameService.class).to(DefaultGameService.class);
		bind(CommandEngine.class).asEagerSingleton();
		
		install(new ShivasDatabaseModule());
	}

}
