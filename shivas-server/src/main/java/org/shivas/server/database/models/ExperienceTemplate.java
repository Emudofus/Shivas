package org.shivas.server.database.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="experience")
public class ExperienceTemplate implements Serializable {

	private static final long serialVersionUID = 3514775200724344009L;
	
	@Id
	private short level;
	
	@Column(nullable=false)
	private long character;
	
	@Transient
	private ExperienceTemplate previous;
	
	@Transient
	private ExperienceTemplate next;

	/**
	 * @return the level
	 */
	public short getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(short level) {
		this.level = level;
	}

	/**
	 * @return the character
	 */
	public long getCharacter() {
		return character;
	}

	/**
	 * @param character the character to set
	 */
	public void setCharacter(long character) {
		this.character = character;
	}

	/**
	 * @return the previous
	 */
	public ExperienceTemplate getPrevious() {
		return previous;
	}

	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(ExperienceTemplate previous) {
		this.previous = previous;
	}

	/**
	 * @return the next
	 */
	public ExperienceTemplate getNext() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(ExperienceTemplate next) {
		this.next = next;
	}

}
