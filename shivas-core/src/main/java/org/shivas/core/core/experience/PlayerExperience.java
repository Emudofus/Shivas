package org.shivas.core.core.experience;

import java.io.Serializable;

public class PlayerExperience implements Serializable, Experience<Long> {

	private static final long serialVersionUID = 1733336784463819181L;
	
	private org.shivas.data.entity.Experience template;
	private long experience;
	
	public PlayerExperience() {
	}

	public PlayerExperience(org.shivas.data.entity.Experience template) {
		this.template = template;
		this.experience = this.template.getPlayer();
	}

	public PlayerExperience(org.shivas.data.entity.Experience template, long experience) {
		this.template = template;
		this.experience = experience;
	}

	public short level() {
		return template.getLevel();
	}
	
	public void setLevel(short level) {
		if (level <= 0) return;
		
		if (level() < level) addLevel((short) (level - level()));
		else if (level() > level) removeLevel((short) (level() - level));
	}

	public void addLevel(short level) {
		if (level < 0) {
			removeLevel(level);
			return;
		}
		
		while (level() < level) {
			template = template.next();
		}
		
		experience = template.getPlayer();
	}

	public void removeLevel(short level) {
		if (level < 0) {
			addLevel(level);
			return;
		}
		
		while (level() > level) {
			template = template.previous();
		}
	}

	public Long current() {
		return experience;
	}

	public Long min() {
		return template.getPlayer();
	}

	public Long max() {
		return template.next().getPlayer();
	}

	public void plus(Long experience) {
		this.experience += experience;
		
		while (this.experience > max()) {
			template = template.next();
		}
	}

	public void minus(Long experience) {
		this.experience -= experience;
		
		while (this.experience < min()) {
			template = template.previous();
		}
	}

}
