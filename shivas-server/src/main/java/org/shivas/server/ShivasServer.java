package org.shivas.server;

import org.shivas.server.database.annotations.DefaultDatabase;
import org.shivas.server.database.annotations.StaticDatabase;
import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.persist.PersistService;

public class ShivasServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasServer.class);

	private PersistService psStatic, psDefault;
	private GameService gs;
	private LoginService ls;
	
	public void start() {
		Injector inject = Guice.createInjector(new ShivasModule());

		psStatic = inject.getInstance(Key.get(PersistService.class, StaticDatabase.class));
		psDefault = inject.getInstance(Key.get(PersistService.class, DefaultDatabase.class));

		psStatic.start();
		psDefault.start();

		gs = inject.getInstance(GameService.class);
		ls = inject.getInstance(LoginService.class);

		gs.start();
		ls.start();
		
		log.info("started");
	}
	
	public void stop() {
		ls.stop();
		gs.stop();
		psDefault.stop();
		psStatic.stop();
		
		log.info("stopped");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ShivasServer server = new ShivasServer();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				server.stop();
			}
		}));
		
		server.start();
	}

}
