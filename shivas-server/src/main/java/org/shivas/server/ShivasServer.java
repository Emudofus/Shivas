package org.shivas.server;

import org.atomium.EntityManager;
import org.shivas.server.core.plugins.PluginsManager;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.LoginService;
import org.shivas.server.utils.ContainerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ShivasServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasServer.class);

	private EntityManager em;
	private RepositoryContainer repos;
	private GameService gs;
	private LoginService ls;
	private PluginsManager pm;
	private ContainerProvider cp;
	
	public ShivasServer(Module... modules) {
		Injector inject = Guice.createInjector(modules);
		em = inject.getInstance(EntityManager.class);
		repos = inject.getInstance(RepositoryContainer.class);
		gs = inject.getInstance(GameService.class);
		ls = inject.getInstance(LoginService.class);
		pm = inject.getInstance(PluginsManager.class);
		cp = inject.getInstance(ContainerProvider.class);
	}
	
	public void start() {
		pm.load(); // load plugins
		cp.load(); // load static data
		
		em.start(); // start entity manager
		repos.load(); // load users' data
		
		gs.start(); // start game server
		ls.start(); // start login server
		
		pm.start(); // start plugins
		
		log.info("started");
	}
	
	public void stop() {
		pm.stop(); // stop plugins
		
		ls.stop(); // stop login server
		gs.stop(); // stop game server
		
		repos.close(); // release users' data
		em.stop(); // stop entity manager
		
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
