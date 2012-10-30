package org.shivas.core.modules;

import org.shivas.data.*;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.core.config.*;
import org.shivas.core.core.actions.ShivasActionFactory;
import org.shivas.core.core.castables.effects.DefaultEffectFactory;
import org.shivas.core.core.castables.effects.EffectFactory;
import org.shivas.core.core.commands.CommandEngine;
import org.shivas.core.database.*;
import org.shivas.core.services.game.*;
import org.shivas.core.services.login.*;
import org.shivas.core.utils.ContainerProvider;

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
        install(new ShivasCommandModule());
	}

}
