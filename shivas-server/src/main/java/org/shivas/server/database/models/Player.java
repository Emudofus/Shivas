package org.shivas.server.database.models;

import java.io.Serializable;

import org.atomium.PersistableEntity;
import org.shivas.data.entity.Breed;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.protocol.client.types.BaseCharacterType;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.RolePlayCharacterType;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.Location;
import org.shivas.server.core.PlayerLook;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.experience.PlayerExperience;
import org.shivas.server.core.items.PlayerBag;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.core.spells.SpellList;
import org.shivas.server.core.statistics.PlayerStatistics;
import org.shivas.server.services.game.GameClient;

public class Player implements Serializable, PersistableEntity<Integer>, GameActor {

	private static final long serialVersionUID = -5864467711777891397L;
	
	private int id;
	private Account owner;
	private String name;
	private Breed breed;
	private Gender gender;
	private PlayerLook look;
	private PlayerExperience experience;
	private Location location;
	private PlayerStatistics stats;
	private PlayerBag bag;
	private SpellList spells;
	
	private final EventDispatcher event = EventDispatchers.create();
	
	private GameClient client;

	public Player(String name, Breed breed, Gender gender,
			PlayerExperience experience, Location location) {
		this.name = name;
		this.breed = breed;
		this.gender = gender;
		this.experience = experience;
		this.location = location;
	}

	public Player(int id, Account owner, String name,
			Breed breed, Gender gender,
			PlayerExperience experience, Location location) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.breed = breed;
		this.gender = gender;
		this.experience = experience;
		this.location = location;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	public int getPublicId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the owner
	 */
	public Account getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Account owner) {
		this.owner = owner;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the breed
	 */
	public Breed getBreed() {
		return breed;
	}

	/**
	 * @param breed the breed to set
	 */
	public void setBreed(Breed breed) {
		this.breed = breed;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @return the look
	 */
	public PlayerLook getLook() {
		return look;
	}

	/**
	 * @param look the look to set
	 */
	public void setLook(PlayerLook look) {
		this.look = look;
	}

	/**
	 * @return the experience
	 */
	public PlayerExperience getExperience() {
		return experience;
	}

	/**
	 * @param experience the experience to set
	 */
	public void setExperience(PlayerExperience experience) {
		this.experience = experience;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	public PlayerStatistics getStats() {
		return stats;
	}

	public void setStats(PlayerStatistics stats) {
		this.stats = stats;
	}

	/**
	 * @return the bag
	 */
	public PlayerBag getBag() {
		return bag;
	}

	/**
	 * @param bag the bag to set
	 */
	public void setBag(PlayerBag bag) {
		this.bag = bag;
	}

	/**
	 * @return the spells
	 */
	public SpellList getSpells() {
		return spells;
	}

	/**
	 * @param spells the spells to set
	 */
	public void setSpells(SpellList spells) {
		this.spells = spells;
	}

	public EventDispatcher getEvent() {
		return event;
	}

	/**
	 * @return the client
	 */
	public GameClient getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(GameClient client) {
		this.client = client;
	}

	public void teleport(GameMap map, short cell) {
		location.getMap().event().unsubscribe(client.eventListener());
		location.getMap().leave(client.player());
		
		location.setMap(map);
		location.setCell(cell);
		
		client.write(GameMessageFormatter.changeMapMessage(client.player().getPublicId()));
		client.write(GameMessageFormatter.mapDataMessage(
				client.player().getLocation().getMap().getId(),
				client.player().getLocation().getMap().getDate(),
				client.player().getLocation().getMap().getKey()
		));
	}
	
	public boolean canReceiveMessageFrom(Player source) {
		return !source.getOwner().getContacts().hasContact(owner, Contact.Type.ENNEMY) ||
			   !owner.getContacts().hasContact(source.getOwner(), Contact.Type.ENNEMY);
	}
	
	public void sendMessage(Player source, String message) {
		client.write(ChannelGameMessageFormatter.clientPrivateMessage(true, source.id, source.name, message));
		source.client.write(ChannelGameMessageFormatter.clientPrivateMessage(false, id, name, message));
	}
	
	public String url() {
		return InfoGameMessageFormatter.urlize(name);
	}

	public int hashCode() {
		return id;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Player))
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public BaseCharacterType toBaseCharacterType() {
		return new BaseCharacterType(
				id,
				name,
				experience.level(),
				look.skin(),
				look.colors().first(),
				look.colors().second(),
				look.colors().third(),
				look.accessories(),
				false // TODO store
		);
	}

	public BaseRolePlayActorType toBaseRolePlayActorType() {
		return new RolePlayCharacterType(
				id,
				name,
				(byte) breed.getId(),
				look.skin(),
				look.size(),
				gender,
				experience.level(),
				look.colors().first(),
				look.colors().second(),
				look.colors().third(),
			    look.accessories(),
				location.getCell(),
				location.getOrientation(),
				false, // TODO guilds
				null,
				null
		);
	}

}
