package org.shivas.server.core.experience;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

import org.shivas.server.database.models.ExperienceTemplate;

@Embeddable
public class PlayerExperience implements Serializable, Experience<Long> {

	private static final long serialVersionUID = 1733336784463819181L;
	
	@JoinColumn(name="level", nullable=false)
	private ExperienceTemplate template;
	
	@Column(nullable=false)
	private long experience;
	
	public PlayerExperience() {
	}

	public PlayerExperience(ExperienceTemplate template) {
		this.template = template;
		this.experience = this.template.getPlayer();
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
