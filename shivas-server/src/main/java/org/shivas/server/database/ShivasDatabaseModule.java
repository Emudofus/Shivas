package org.shivas.server.database;

import org.atomium.EntityManager;
import org.atomium.repository.*;
import org.shivas.server.database.models.*;
import org.shivas.server.database.repositories.*;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class ShivasDatabaseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EntityManager.class).to(ShivasEntityManager.class);
		bind(RepositoryContainer.class).to(DefaultRepositoryContainer.class);
		
		bind(new TypeLiteral<BaseEntityRepository<Integer, Account>>(){}).to(AccountRepository.class);
		
		bind(new TypeLiteral<BaseEntityRepository<Integer, Player>>(){}).to(PlayerRepository.class);
		bind(new TypeLiteral<EntityRepository<Integer, Player>>(){}).to(PlayerRepository.class);
		
		bind(new TypeLiteral<BaseEntityRepository<Long, GameItem>>(){}).to(GameItemRepository.class);
		bind(new TypeLiteral<EntityRepository<Long, GameItem>>(){}).to(GameItemRepository.class);
		
		bind(new TypeLiteral<BaseEntityRepository<Long, Spell>>(){}).to(SpellRepository.class);
		bind(new TypeLiteral<EntityRepository<Long, Spell>>(){}).to(SpellRepository.class);
		
		bind(new TypeLiteral<BaseEntityRepository<Long, Contact>>(){}).to(ContactRepository.class);
		bind(new TypeLiteral<EntityRepository<Long, Contact>>(){}).to(ContactRepository.class);

        bind(new TypeLiteral<BaseEntityRepository<Long, StoredItem>>(){}).to(StoredItemRepository.class);
        bind(new TypeLiteral<EntityRepository<Long, StoredItem>>(){}).to(StoredItemRepository.class);

        bind(new TypeLiteral<BaseEntityRepository<Integer, Guild>>(){}).to(GuildRepository.class);
        bind(new TypeLiteral<EntityRepository<Integer, Guild>>(){}).to(GuildRepository.class);

        bind(new TypeLiteral<BaseEntityRepository<Integer, GuildMember>>(){}).to(GuildMemberRepository.class);
        bind(new TypeLiteral<EntityRepository<Integer, GuildMember>>(){}).to(GuildMemberRepository.class);
	}

}
