package org.shivas.data.entity;

import java.io.Serializable;

public class SpellBreed implements Serializable {

	private static final long serialVersionUID = 8492391480997050113L;
	
	private Breed breed;
	private SpellTemplate template;
	private int minLevel;
	private int position;
	
	public SpellBreed() {
	}

	public SpellBreed(Breed breed, SpellTemplate template, int minLevel, int position) {
		this.breed = breed;
		this.template = template;
		this.minLevel = minLevel;
		this.position = position;
	}

	public Breed getBreed() {
		return breed;
	}

	public void setBreed(Breed breed) {
		this.breed = breed;
	}

	public SpellTemplate getTemplate() {
		return template;
	}

	public void setTemplate(SpellTemplate template) {
		this.template = template;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
