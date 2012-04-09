package org.shivas.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

public class ShivasGameServer {
	
	private static final Logger log = LoggerFactory.getLogger(ShivasGameServer.class);

	public static final String UNIT_PERSIST_NAME = "game";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Injector inject = Guice.createInjector(new ShivasGameModule());
		
		final PersistService ps = inject.getInstance(PersistService.class);
		ps.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				ps.stop();
				
				log.info("stopped");
			}
		}));
		
		log.info("started");
	}

}
