package org.shivas.server;

import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;

public class ShivasServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasServer.class);
	
	private final Module[] modules;

	private PersistService ps;
	private GameService gs;
	private LoginService ls;
	
	public ShivasServer(Module... modules) {
		this.modules = modules;
	}
	
	public void start() {
		Injector inject = Guice.createInjector(modules);

		ps = inject.getInstance(PersistService.class);
		ps.start();

		gs = inject.getInstance(GameService.class);
		ls = inject.getInstance(LoginService.class);

		gs.start();
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
		final ShivasServer server = new ShivasServer(new ShivasModule());
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				server.stop();
			}
		}));
		
		server.start();
	}

}
