package org.shivas.login;

import org.shivas.common.crypto.Cipher;
import org.shivas.login.config.DefaultLoginConfig;
import org.shivas.login.config.LoginConfig;
import org.shivas.login.database.AccountPasswordCipherProvider;
import org.shivas.login.database.RepositoryContainer;
import org.shivas.login.database.DefaultRepositoryContainer;
import org.shivas.login.services.DefaultGameService;
import org.shivas.login.services.DefaultLoginService;
import org.shivas.login.services.GameService;
import org.shivas.login.services.LoginService;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.google.inject.persist.jpa.JpaPersistModule;

public class ShivasLoginModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule(ShivasLoginServer.UNIT_PERSIST_NAME));
		
		bind(LoginConfig.class).to(DefaultLoginConfig.class);
		
		bind(LoginService.class).to(DefaultLoginService.class);
		
		bind(GameService.class).to(DefaultGameService.class);
		
		bind(RepositoryContainer.class).to(DefaultRepositoryContainer.class);
		
		bind(Cipher.class)
		    .annotatedWith(Names.named("account.password.cipher"))
		    .toProvider(AccountPasswordCipherProvider.class);
	}

}
