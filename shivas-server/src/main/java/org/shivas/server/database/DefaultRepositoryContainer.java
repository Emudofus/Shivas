package org.shivas.server.database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.Timer;

import org.atomium.exception.LoadingException;
import org.shivas.server.config.Config;
import org.shivas.server.database.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultRepositoryContainer.class);
	
	@Inject
	private Config config;
	
	@Inject
	private AccountRepository accounts;
	
	@Inject
	private PlayerRepository players;
	
	private Timer timer;
	
	public void load() {
		try {
			log.debug("{} accounts loaded", accounts.load());
			log.debug("{} players loaded", players.load());
		} catch (LoadingException e) {
			log.error("can't load because of {}", e.getMessage());
		}
		
		startTimer();
	}
	
	private void startTimer() {
		timer = new Timer(config.databaseSaveDelay() * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		timer.start();
	}

	public void save() {
		players.save();
	}

	public AccountRepository accounts() {
		return accounts;
	}
	
	public PlayerRepository players() {
		return players;
	}

}
