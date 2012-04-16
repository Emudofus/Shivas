package org.shivas.server.core.experience;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PlayerExperience implements Serializable, Experience<Long> {

	private static final long serialVersionUID = 1733336784463819181L;
	
	@Column(nullable=false)
	private short level;
	
	@Column(nullable=false)
	private long experience;

	public short level() {
		return level;
	}

	public void addLevel(short level) {
		this.level += level;
		// TODO set valid experience
	}

	public void removeLevel(short level) {
		this.level -= level;
		// TODO set valid experience
	}

	public Long current() {
		return experience;
	}

	public Long min() {
		return (long)0; // TODO return valid experience
	}

	public Long max() {
		return (long)1; // TODO return valid experience
	}

	public void plus(Long experience) {
		this.experience += experience;
		// TODO set valid level
	}

	public void minus(Long experience) {
		this.experience -= experience;
		// TODO set valid level
	}

}
