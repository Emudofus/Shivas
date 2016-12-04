package org.shivas.core.database.models;

import org.atomium.PersistableEntity;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.entity.Breed;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.protocol.client.types.BaseCharacterType;
import org.shivas.protocol.client.types.BasePartyMemberType;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.RolePlayCharacterType;
import org.shivas.core.core.GameActor;
import org.shivas.core.core.Location;
import org.shivas.core.core.PlayerLook;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.events.EventDispatchers;
import org.shivas.core.core.events.events.PlayerTeleportationEvent;
import org.shivas.core.core.experience.PlayerExperience;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.PlayerFighter;
import org.shivas.core.core.items.PlayerBag;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.core.spells.SpellList;
import org.shivas.core.core.statistics.PlayerStatistics;
import org.shivas.core.core.stores.PlayerStore;
import org.shivas.core.core.waypoints.WaypointList;
import org.shivas.core.services.game.GameClient;

import java.io.Serializable;

public class Player implements Serializable, PersistableEntity<Integer>, GameActor {

	private static final long serialVersionUID = -5864467711777891397L;
	
	private int id;
	private Account owner;
	private String name;
	private Breed breed;
	private Gender gender;
	private PlayerLook look;
	private PlayerExperience experience;
	private Location location, savedLocation;
	private PlayerStatistics stats;
	private PlayerBag bag;
	private SpellList spells;
	private WaypointList waypoints;
    private PlayerStore store;
    private GuildMember guildMember;
    private PlayerFighter fighter;
	
	private final EventDispatcher event = EventDispatchers.create();
	
	private GameClient client;

	public Player(String name, Breed breed, Gender gender,
			PlayerExperience experience, Location location, Location savedLocation) {
		this.name = name;
		this.breed = breed;
		this.gender = gender;
		this.experience = experience;
		this.location = location;
		this.savedLocation = savedLocation;
	}

	public Player(int id, Account owner, String name,
			Breed breed, Gender gender,
			PlayerExperience experience, Location location, Location savedLocation) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.breed = breed;
		this.gender = gender;
		this.experience = experience;
		this.location = location;
		this.savedLocation = savedLocation;
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

	public Location getSavedLocation() {
		return savedLocation;
	}

	public void setSavedLocation(Location savedLocation) {
		this.savedLocation = savedLocation;
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

	public WaypointList getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(WaypointList waypoints) {
		this.waypoints = waypoints;
	}

    public PlayerStore getStore() {
        return store;
    }

    public void setStore(PlayerStore store) {
        this.store = store;
    }

    public GuildMember getGuildMember() {
        return guildMember;
    }

    public void setGuildMember(GuildMember guildMember) {
        this.guildMember = guildMember;
    }

    public Guild getGuild() {
        return guildMember.getGuild();
    }

    public PlayerFighter getFighter() {
        return fighter;
    }

    public void setFighter(PlayerFighter fighter) {
        this.fighter = fighter;
    }

    public Fight getFight() {
        return fighter.getFight();
    }

    public boolean isFighting() {
        return fighter != null;
    }

    public boolean hasGuild() {
        return guildMember != null;
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
		event.publish(new PlayerTeleportationEvent(this, new Location(
				map,
				cell,
				location.getOrientation()
		)));
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
        return id == other.id;
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
				owner.getCurrentStore() == store
		);
	}
	
	public BasePartyMemberType toBasePartyMemberType() {
		return new BasePartyMemberType(
				id,
				name,
				look.skin(),
				look.colors().first(),
				look.colors().second(),
				look.colors().third(),
				bag.accessoriesTemplateId(),
				(short) stats.life().current(),
				(short) stats.life().max(),
				experience.level(),
				stats.get(CharacteristicType.Initiative).safeTotal(),
				stats.get(CharacteristicType.Prospection).safeTotal(),
				(short) 0 // TODO alignment
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
				hasGuild(),
				hasGuild() ? getGuild().getName() : null,
				hasGuild() ? getGuild().getEmblem() : null
		);
	}
}
