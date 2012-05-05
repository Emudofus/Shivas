package org.shivas.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.atomium.EntityManager;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ShivasServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasServer.class);
	public static final ExecutorService EVENT_WORKER = Executors.newSingleThreadExecutor();
	
	private final Module[] modules;

	private EntityManager em;
	private RepositoryContainer repos;
	private GameService gs;
	private LoginService ls;
	
	public ShivasServer(Module... modules) {
		this.modules = modules;
	}
	
	public void start() {
		Injector inject = Guice.createInjector(modules);

		em = inject.getInstance(EntityManager.class);
		em.start();
		
		repos = inject.getInstance(RepositoryContainer.class);
		repos.load();

		gs = inject.getInstance(GameService.class);
		ls = inject.getInstance(LoginService.class);

		gs.start();
		ls.start();
		
		log.info("started");
	}
	
	public void stop() {
		ls.stop();
		gs.stop();
		em.stop();
		
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
