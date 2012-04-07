package org.shivas.login;

import org.shivas.login.config.DefaultLoginConfig;
import org.shivas.login.config.LoginConfig;
import org.shivas.login.services.DefaultLoginService;
import org.shivas.login.services.LoginService;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class ShivasLoginModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule(ShivasLoginServer.UNIT_PERSIST_NAME));
		bind(LoginConfig.class).to(DefaultLoginConfig.class);
		bind(LoginService.class).to(DefaultLoginService.class);
	}

}
