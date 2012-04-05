package org.shivas.login;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class ShivasLoginModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule(ShivasLoginServer.UNIT_PERSIST_NAME));
	}

}
