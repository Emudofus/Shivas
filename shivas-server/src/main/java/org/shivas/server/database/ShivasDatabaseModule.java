package org.shivas.server.database;

import org.atomium.repository.EntityRepository;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.database.models.Player;
import org.shivas.server.database.repositories.AccountRepository;
import org.shivas.server.database.repositories.GameItemRepository;
import org.shivas.server.database.repositories.PlayerRepository;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class ShivasDatabaseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<EntityRepository<Integer, Account>>(){}).to(AccountRepository.class);
		bind(new TypeLiteral<EntityRepository<Integer, Player>>(){}).to(PlayerRepository.class);
		bind(new TypeLiteral<EntityRepository<Long, GameItem>>(){}).to(GameItemRepository.class);
	}

}
