package org.shivas.core.modules;

import com.google.inject.AbstractModule;
import org.shivas.core.core.actions.ShivasActionFactory;
import org.shivas.core.core.castables.effects.DefaultEffectFactory;
import org.shivas.core.core.castables.effects.EffectFactory;
import org.shivas.core.core.commands.CommandEngine;
import org.shivas.core.database.ShivasEntityFactory;
import org.shivas.core.services.game.DefaultGameService;
import org.shivas.core.services.game.GameService;
import org.shivas.core.services.login.DefaultLoginService;
import org.shivas.core.services.login.LoginService;
import org.shivas.core.utils.ContainerProvider;
import org.shivas.data.Container;
import org.shivas.data.EntityFactory;
import org.shivas.data.entity.factory.ActionFactory;

public class ShivasServiceModule extends AbstractModule {

	@Override
	protected void configure() {
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
