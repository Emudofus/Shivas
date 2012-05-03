package org.shivas.data.entity;

import java.io.Serializable;

public class Experience implements Serializable {
	
	private static final long serialVersionUID = -1487686495878618902L;
	
	private short level;
	private long player;
	private Experience previous;
	private Experience next;

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public long getPlayer() {
		return player;
	}

	public void setPlayer(long player) {
		this.player = player;
	}

	public Experience getPrevious() {
		return previous;
	}

	public void setPrevious(Experience previous) {
		this.previous = previous;
	}

	public Experience getNext() {
		return next;
	}

	public void setNext(Experience next) {
		this.next = next;
	}

}
