package org.shivas.server.database;

import org.atomium.repository.*;
import org.shivas.server.database.models.*;
import org.shivas.server.database.repositories.*;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class ShivasDatabaseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<EntityRepository<Integer, Account>>(){}).to(AccountRepository.class);
		bind(new TypeLiteral<EntityRepository<Integer, Player>>(){}).to(PlayerRepository.class);
		bind(new TypeLiteral<EntityRepository<Long, GameItem>>(){}).to(GameItemRepository.class);
		bind(new TypeLiteral<PersistableEntityRepository<Long, GameItem>>(){}).to(GameItemRepository.class);
		bind(new TypeLiteral<EntityRepository<Long, Spell>>(){}).to(SpellRepository.class);
		bind(new TypeLiteral<PersistableEntityRepository<Long, Spell>>(){}).to(SpellRepository.class);
	}

}
