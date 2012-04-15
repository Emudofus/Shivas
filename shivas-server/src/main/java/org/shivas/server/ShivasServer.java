package org.shivas.server;

import org.shivas.server.database.annotations.DefaultDatabase;
import org.shivas.server.database.annotations.StaticDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.persist.PersistService;

public class ShivasServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasServer.class);

	private PersistService psStatic, psDefault;
	
	public void start() {
		Injector inject = Guice.createInjector(new ShivasModule());

		psStatic = inject.getInstance(Key.get(PersistService.class, StaticDatabase.class));
		psDefault = inject.getInstance(Key.get(PersistService.class, DefaultDatabase.class));

		psStatic.start();
		psDefault.start();
		
		log.info("started");
	}
	
	public void stop() {
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
