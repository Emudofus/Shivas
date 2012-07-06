package org.shivas.data.entity;

import java.io.Serializable;

public class SpellTemplate implements Serializable {

	private static final long serialVersionUID = 1449677293634775838L;
	
	private short id;
	private short sprite;
	private String spriteInfos;
	private SpellLevel[] levels;
	
	/**
	 * @return the id
	 */
	public short getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(short id) {
		this.id = id;
	}
	/**
	 * @return the sprite
	 */
	public short getSprite() {
		return sprite;
	}
	/**
	 * @param sprite the sprite to set
	 */
	public void setSprite(short sprite) {
		this.sprite = sprite;
	}
	/**
	 * @return the spriteInfos
	 */
	public String getSpriteInfos() {
		return spriteInfos;
	}
	/**
	 * @param spriteInfos the spriteInfos to set
	 */
	public void setSpriteInfos(String spriteInfos) {
		this.spriteInfos = spriteInfos;
	}
	/**
	 * @return the levels
	 */
	public SpellLevel[] getLevels() {
		return levels;
	}
	/**
	 * @param levels the levels to set
	 */
	public void setLevels(SpellLevel[] levels) {
		this.levels = levels;
	}

}
