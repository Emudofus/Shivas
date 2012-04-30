package org.shivas.data.entity;

import java.io.Serializable;

public class Experience implements Serializable {
	
	private static final long serialVersionUID = -1487686495878618902L;
	
	private short level;
	private long player;
	private Experience previous;
	private Experience next;
	
	public Experience(short level, long player) {
		this.level = level;
		this.player = player;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return level;
	}

	/**
	 * @return the level
	 */
	public short getLevel() {
		return level;
	}

	/**
	 * @return the character
	 */
	public long getPlayer() {
		return player;
	}

	/**
	 * @return the previous
	 */
	public Experience previous() {
		return previous;
	}

	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(Experience previous) {
		this.previous = previous;
	}

	/**
	 * @return the next
	 */
	public Experience next() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(Experience next) {
		this.next = next;
	}

}
