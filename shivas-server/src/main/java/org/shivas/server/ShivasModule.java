package org.shivas.server;

import org.shivas.common.inject.AnnotatedJpaPersistModule;
import org.shivas.server.database.DefaultRepositoryContainer;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.database.annotations.DefaultDatabase;
import org.shivas.server.database.annotations.StaticDatabase;

import com.google.inject.AbstractModule;

public class ShivasModule extends AbstractModule {

	@Override
	protected void configure() {
		install(AnnotatedJpaPersistModule.newModule("default", DefaultDatabase.class));
		install(AnnotatedJpaPersistModule.newModule("static", StaticDatabase.class));
		
		bind(RepositoryContainer.class).to(DefaultRepositoryContainer.class);
	}

}
