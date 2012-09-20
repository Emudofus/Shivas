package org.shivas.server.database;

import org.atomium.exception.LoadingException;
import org.shivas.server.config.Config;
import org.shivas.server.database.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultRepositoryContainer.class);
	
	@Inject
	private Config config;
	
	@Inject
	private AccountRepository accounts;
	
	@Inject
	private PlayerRepository players;
	
	@Inject
	private GameItemRepository items;
	
	@Inject
	private SpellRepository spells;
	
	@Inject
	private ContactRepository contacts;
	
	@Inject
	private GiftRepository gifts;

    @Inject
    private StoredItemRepository storedItems;
	
	private Timer timer;
	
	public void load() {
		try {
			log.debug("{} accounts loaded", accounts.load());
			log.debug("{} contacts loaded", contacts.load());
			log.debug("{} players loaded", players.load());
			log.debug("{} items loaded", items.load());
			log.debug("{} spells loaded", spells.load());
			log.debug("{} gifts loaded", gifts.load());
            log.debug("{} stored items loaded", storedItems.load());
		} catch (LoadingException e) {
			log.error("can't load because of {}", e.getMessage());
		}
		
		startTimer();
	}
	
	public void close() {
		timer.stop();
		accounts.stop();
		
		save();
	}
	
	private void startTimer() {
		timer = new Timer(config.databaseSaveDelay() * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		timer.start();
		accounts.start();
	}

	public void save() {
		accounts.save();
		contacts.save();
		players.save();
		items.save();
		spells.save();
	}

	public AccountRepository accounts() {
		return accounts;
	}
	
	public PlayerRepository players() {
		return players;
	}

	@Override
	public GameItemRepository items() {
		return items;
	}

	@Override
	public SpellRepository spells() {
		return spells;
	}

	@Override
	public ContactRepository contacts() {
		return contacts;
	}

	@Override
	public GiftRepository gifts() {
		return gifts;
	}

    @Override
    public StoredItemRepository storedItems() {
        return storedItems;
    }

}
