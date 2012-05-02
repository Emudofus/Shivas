package org.shivas.server.database.models;

import java.io.Serializable;
import java.util.Collection;

import org.atomium.PersistableEntity;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.GameMap;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.types.BaseCharacterType;
import org.shivas.server.core.Colors;
import org.shivas.server.core.experience.PlayerExperience;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class Player implements Serializable, PersistableEntity<Integer> {

	private static final long serialVersionUID = -5864467711777891397L;
	
	private int id;
	private Account owner;
	private String name;
	private Breed breed;
	private Gender gender;
	private short skin;
	private Colors colors;
	private PlayerExperience experience;
	private GameMap map;
	private short cell;
	
	public Player() {
	}

	public Player(Account owner, String name, Breed breed, Gender gender,
			short skin, Colors colors, PlayerExperience experience,
			GameMap map, short cell) {
		this.owner = owner;
		this.name = name;
		this.breed = breed;
		this.gender = gender;
		this.skin = skin;
		this.colors = colors;
		this.experience = experience;
		this.map = map;
		this.cell = cell;
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
	 * @return the skin
	 */
	public short getSkin() {
		return skin;
	}

	/**
	 * @param skin the skin to set
	 */
	public void setSkin(short skin) {
		this.skin = skin;
	}

	/**
	 * @return the colors
	 */
	public Colors getColors() {
		return colors;
	}

	/**
	 * @param colors the colors to set
	 */
	public void setColors(Colors colors) {
		this.colors = colors;
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
	 * @return the map
	 */
	public GameMap getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(GameMap map) {
		this.map = map;
	}

	/**
	 * @return the cell
	 */
	public short getCell() {
		return cell;
	}

	/**
	 * @param cell the cell to set
	 */
	public void setCell(short cell) {
		this.cell = cell;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
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
				skin,
				colors.first(),
				colors.second(),
				colors.third(),
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

}
