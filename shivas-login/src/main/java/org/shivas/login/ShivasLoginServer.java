package org.shivas.login;

import org.shivas.login.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

public class ShivasLoginServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasLoginServer.class);

	public static final String UNIT_PERSIST_NAME = "login";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Injector inject = Guice.createInjector(new ShivasLoginModule());
		
		final PersistService ps = inject.getInstance(PersistService.class);
		ps.start(); // must be started before instantiate LoginService
		
		final LoginService ls = inject.getInstance(LoginService.class);
		ls.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				ls.stop();
				ps.stop();
				
				log.info("stopped");
			}
		}));
		
		log.info("started");		
	}

}
