package org.shivas.game;

import org.shivas.game.services.GameService;
import org.shivas.game.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

public class ShivasGameServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasGameServer.class);
	public static final String UNIT_PERSIST_NAME = "game";
	
	private PersistService ps;
	private LoginService ls;
	private GameService gs;
	
	public void start() {
		Injector inject = Guice.createInjector(new ShivasGameModule());
		
		ps = inject.getInstance(PersistService.class);
		ps.start();
		
		ls = inject.getInstance(LoginService.class);
		ls.start();
		
		gs = inject.getInstance(GameService.class);
		gs.start();
		
		log.info("started");
	}
	
	public void stop() {
		gs.stop();
		ls.stop();
		ps.stop();
		
		log.info("stopped");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ShivasGameServer server = new ShivasGameServer();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				server.stop();
			}
		}));
		
		server.start();
	}

}
