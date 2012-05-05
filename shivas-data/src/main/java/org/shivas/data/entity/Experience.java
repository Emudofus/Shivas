package org.shivas.data.entity;

import java.io.Serializable;

public class Experience implements Serializable {
	
	private static final long serialVersionUID = -1487686495878618902L;
	
	private short level;
	private long player;
	private int job;
	private int mount;
	private short alignment;
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

	public int getJob() {
		return job;
	}

	public void setJob(int job) {
		this.job = job;
	}

	public int getMount() {
		return mount;
	}

	public void setMount(int mount) {
		this.mount = mount;
	}

	public short getAlignment() {
		return alignment;
	}

	public void setAlignment(short alignment) {
		this.alignment = alignment;
	}

	public Experience previous() {
		return previous;
	}

	public void setPrevious(Experience previous) {
		this.previous = previous;
	}

	public Experience next() {
		return next;
	}

	public void setNext(Experience next) {
		this.next = next;
	}

}
