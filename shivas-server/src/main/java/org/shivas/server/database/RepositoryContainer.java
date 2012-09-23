package org.shivas.server.database;

import org.shivas.server.database.repositories.*;

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
