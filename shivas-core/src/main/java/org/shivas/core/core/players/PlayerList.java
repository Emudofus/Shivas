package org.shivas.core.core.players;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import org.shivas.core.config.ConfigProvider;
import org.shivas.core.database.models.Account;
import org.shivas.core.database.models.Player;
import org.shivas.core.database.repositories.PlayerRepository;
import org.shivas.core.utils.Converters;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.types.BaseCharacterType;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class PlayerList implements Iterable<Player> {

	private final Account owner;
	private final PlayerRepository repo;
    private final ConfigProvider config;
	private final Map<Integer, Player> players = Maps.newHashMap();
	
	public PlayerList(Account owner, PlayerRepository repo, ConfigProvider config) {
		this.owner = owner;
		this.repo = repo;
        this.config = config;
    }

	/**
	 * returns list's owner
	 * @return list's owner
	 */
	public Account getOwner() {
		return owner;
	}
	
	/**
	 * returns the player by its id, null if unknown 
	 * @param id player's id
	 * @return
	 */
	public Player get(int id) {
		return players.get(id);
	}

	/**
	 * only add the player in the list
	 * @param player
	 */
	public void add(Player player) {
		players.put(player.getId(), player);
	}

    /**
     * create and persist the player in the database
     * @param name player's name
     * @param breed player's breed
     * @param gender player's gender
     * @param color1 player's first color
     * @param color2 player's second color
     * @param color3 player's third color
     * @return created player
     * @throws SecuredPersistException
     */
    public Player persist(String name, int breed, Gender gender, int color1, int color2, int color3) throws SecuredPersistException {
        if (players.size() >= config.getInt("world.max_players_per_account")) {
            throw new SecuredPersistException(SecuredPersistException.Reason.FULL_ACCOUNT);
        }
        if (repo.nameExists(name)) {
            throw new SecuredPersistException(SecuredPersistException.Reason.NAME_ALREADY_EXISTS);
        }

        Player player = repo.createDefault(name, breed, gender, color1, color2, color3);
        player.setOwner(owner);

        repo.persistLater(player);
        add(player);

        return player;
    }
	
	/**
	 * only remove the player from the list
	 * @param id player's id
	 * @return true if the list contained the player
	 */
	public boolean remove(int id) {
		return players.remove(id) != null;
	}
	
	/**
	 * only remove the player from the list
	 * @param player
	 */
	public void remove(Player player) {
		remove(player.getId());
	}
	
	/**
	 * delete player from the database and remove it from the list
	 * @param player
	 */
	public void delete(Player player, String secretAnswer) throws SecuredDeleteException {
        if (player.getExperience().level() < config.getShort("world.delete_answer_level_needed")) {
            throw new SecuredDeleteException(SecuredDeleteException.Reason.TOO_LOW_PLAYER_LEVEL);
        }
        if (!owner.getSecretAnswer().equalsIgnoreCase(secretAnswer)) {
            throw new SecuredDeleteException(SecuredDeleteException.Reason.BAD_SECRET_ANSWER);
        }

		repo.deleteLater(player);
		remove(player);
	}
	
	public int count() {
		return players.size();
	}
	
	public Collection<BaseCharacterType> toBaseCharacterType() {
		return Collections2.transform(players.values(), Converters.PLAYER_TO_BASECHARACTERTYPE);
	}

	@Override
	public Iterator<Player> iterator() {
		return players.values().iterator();
	}
	
}
