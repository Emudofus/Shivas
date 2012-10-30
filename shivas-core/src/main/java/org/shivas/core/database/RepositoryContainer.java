package org.shivas.core.database;

import org.shivas.core.database.repositories.*;

public interface RepositoryContainer {
	void load();
	void close();
	void save();
	
	AccountRepository accounts();
	PlayerRepository players();
	GameItemRepository items();
	SpellRepository spells();
	ContactRepository contacts();
	GiftRepository gifts();
    StoredItemRepository storedItems();
    GuildRepository guilds();
    GuildMemberRepository guildMembers();
}
