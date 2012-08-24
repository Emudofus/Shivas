package org.shivas.server.core.players;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.atomium.repository.EntityRepository;
import org.shivas.protocol.client.types.BaseCharacterType;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class PlayerList implements Iterable<Player> {

	private final Account owner;
	private final EntityRepository<Integer, Player> repo;
	private final Map<Integer, Player> players = Maps.newHashMap();
	
	public PlayerList(Account owner, EntityRepository<Integer, Player> repo) {
		this.owner = owner;
		this.repo = repo;
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
	 * persist player in database and add it in the list (this will set player's owner)
	 * @param player
	 */
	public void persist(Player player) {
		player.setOwner(owner);
		
		repo.persistLater(player);
		add(player);
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
	public void delete(Player player) {
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
