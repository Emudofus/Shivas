package org.shivas.server.database.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.shivas.server.core.Colors;

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
	
	@Column(nullable=false)
	private short skin;
	
	@Embedded
	private Colors colors;
	
	public Player() {
	}

	public Player(int id, String name) {
		this.id = id;
		this.name = name;
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

}
