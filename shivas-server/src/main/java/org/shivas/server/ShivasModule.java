package org.shivas.server;

import org.shivas.common.inject.AnnotatedJpaPersistModule;
import org.shivas.server.database.DefaultDatabase;
import org.shivas.server.database.StaticDatabase;

import com.google.inject.AbstractModule;

public class ShivasModule extends AbstractModule {

	@Override
	protected void configure() {
		install(AnnotatedJpaPersistModule.newModule("default", DefaultDatabase.class));
		install(AnnotatedJpaPersistModule.newModule("static", StaticDatabase.class));
	}

}
