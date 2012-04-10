package org.shivas.game;

import org.shivas.game.database.DefaultRepositoryContainer;
import org.shivas.game.database.RepositoryContainer;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class ShivasGameModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule(ShivasGameServer.UNIT_PERSIST_NAME));
		
		bind(RepositoryContainer.class).to(DefaultRepositoryContainer.class);
	}

}
