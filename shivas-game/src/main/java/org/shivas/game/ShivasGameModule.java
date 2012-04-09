package org.shivas.game;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class ShivasGameModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule(ShivasGameServer.UNIT_PERSIST_NAME));
	}

}
