package org.shivas.server.database.models;

import java.io.Serializable;
import java.util.Collection;

import org.atomium.LazyReference;
import org.atomium.PersistableEntity;
import org.shivas.data.entity.Breed;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.protocol.client.types.BaseCharacterType;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.RolePlayCharacterType;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.Location;
import org.shivas.server.core.Look;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.events.PlayerTeleportationEvent;
import org.shivas.server.core.events.events.PrivateMessageEvent;
import org.shivas.server.core.experience.PlayerExperience;
import org.shivas.server.core.maps.GMap;
import org.shivas.server.core.statistics.PlayerStatistics;
import org.shivas.server.services.game.GameClient;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class Player implements Serializable, PersistableEntity<Integer>, GameActor {

	private static final long serialVersionUID = -5864467711777891397L;
	
	private int id;
	private LazyReference<Integer, Account> owner;
	private String name;
	private Breed breed;
	private Gender gender;
	private Look look;
	private PlayerExperience experience;
	private Location location;
	private PlayerStatistics stats;
	
	private final EventDispatcher event = EventDispatchers.create();
	
	private GameClient client;

	public Player(LazyReference<Integer, Account> owner, String name, Breed breed, Gender gender,
			Look look, PlayerExperience experience, Location location) {
		this.owner = owner;
		this.name = name;
		this.breed = breed;
		this.gender = gender;
		this.look = look;
		this.experience = experience;
		this.location = location;
	}

	public Player(int id, LazyReference<Integer, Account> owner, String name,
			Breed breed, Gender gender, Look look,
			PlayerExperience experience, Location location) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.breed = breed;
		this.gender = gender;
		this.look = look;
		this.experience = experience;
		this.location = location;
	}

	/**
	 * @return the id
	 */
	public Integer id() {
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
		return owner.get();
	}
	
	/**
	 * @return the owner's reference
	 */
	public LazyReference<Integer, Account> getOwnerReference() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Account owner) {
		this.owner.set(owner);
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
	public Look getLook() {
		return look;
	}

	/**
	 * @param look the look to set
	 */
	public void setLook(Look look) {
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

	public void teleport(GMap map, short cell) {
		event.publish(new PlayerTeleportationEvent(this, new Location(map, cell, location.getOrientation())));
	}
	
	public void sendMessage(Player source, String message) {
		Event pm = new PrivateMessageEvent(source, this, message);
		
		event.publish(pm);
		source.event.publish(pm);
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
				look.getSkin(),
				look.getColors().first(),
				look.getColors().second(),
				look.getColors().third(),
				null, // TODO items
				false // TODO store
		);
	}
	
	public static Collection<BaseCharacterType> toBaseCharacterType(Collection<Player> players) {
		return Collections2.transform(players, new Function<Player, BaseCharacterType>() {
			public BaseCharacterType apply(Player input) {
				return input.toBaseCharacterType();
			}
		});
	}

	public BaseRolePlayActorType toBaseRolePlayActorType() {
		return new RolePlayCharacterType(
				id,
				name,
				(byte) breed.getId(),
				look.getSkin(),
				look.getSize(),
				gender,
				experience.level(),
				look.getColors().first(),
				look.getColors().second(),
				look.getColors().third(),
				new int[5],
				location.getCell(),
				location.getOrientation(),
				false, // TODO guilds
				null,
				null
		);
	}

}
