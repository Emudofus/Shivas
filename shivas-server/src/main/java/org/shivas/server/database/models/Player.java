package org.shivas.server.database.models;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.types.BaseCharacterType;
import org.shivas.server.core.Colors;
import org.shivas.server.core.experience.PlayerExperience;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

@Entity
@Table(name="players")
public class Player implements Serializable {

	private static final long serialVersionUID = -5864467711777891397L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private int id;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Account owner;
	
	@Column(nullable=false, unique=true)
	private String name;

	@ManyToOne
	@JoinColumn(nullable=false)
	private BreedTemplate breed;
	
	@Column(nullable=false)
	private Gender gender;
	
	@Column(nullable=false)
	private short skin;
	
	@Embedded
	private Colors colors;
	
	@Embedded
	private PlayerExperience experience;
	
	public Player() {
	}

	

	public Player(Account owner, String name, BreedTemplate breed,
			Gender gender, short skin, Colors colors,
			PlayerExperience experience) {
		this.owner = owner;
		this.name = name;
		this.breed = breed;
		this.gender = gender;
		this.skin = skin;
		this.colors = colors;
		this.experience = experience;
	}



	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
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
	public BreedTemplate getBreed() {
		return breed;
	}

	/**
	 * @param breed the breed to set
	 */
	public void setBreed(BreedTemplate breed) {
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
	
	@Override
	public int hashCode() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
