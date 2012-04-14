package org.shivas.login;

import org.shivas.login.services.GameService;
import org.shivas.login.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

public class ShivasLoginServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasLoginServer.class);
	public static final String UNIT_PERSIST_NAME = "login";
	
	private PersistService ps;
	private GameService gs;
	private LoginService ls;
	
	public void start() {
		Injector inject = Guice.createInjector(new ShivasLoginModule());
		
		ps = inject.getInstance(PersistService.class);
		ps.start();
		
		gs = inject.getInstance(GameService.class);
		gs.start();
		
		ls = inject.getInstance(LoginService.class);
		ls.start();
		
		log.info("started");
	}
	
	public void stop() {
		ls.stop();
		gs.stop();
		ps.stop();
		
		log.info("stopped");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ShivasLoginServer server = new ShivasLoginServer();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				server.stop();
			}
		}));
		
		server.start();
	}

}
