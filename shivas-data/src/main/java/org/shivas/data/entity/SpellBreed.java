package org.shivas.data.entity;

import java.io.Serializable;

public class SpellBreed implements Serializable {

	private static final long serialVersionUID = 8492391480997050113L;
	
	private Breed breed;
	private SpellTemplate template;
	private short minLevel;
	private byte position;

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

	public short getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(short minLevel) {
		this.minLevel = minLevel;
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}

}
