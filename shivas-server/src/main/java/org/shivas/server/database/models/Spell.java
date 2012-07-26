package org.shivas.server.database.models;

import java.io.Serializable;

import org.atomium.PersistableEntity;
import org.shivas.common.StringUtils;
import org.shivas.data.entity.SpellLevel;
import org.shivas.data.entity.SpellTemplate;
import org.shivas.protocol.client.types.BaseSpellType;

public class Spell implements Serializable, PersistableEntity<Long> {

	private static final long serialVersionUID = 4763070969120891187L;
	
	private long id;
	private Player player;
	private SpellTemplate template;
	private SpellLevel level;
	private byte position;
	
	public Spell() { }

	public Spell(long id, Player player, SpellTemplate template, SpellLevel level, byte position) {
		this.id = id;
		this.player = player;
		this.template = template;
		this.level = level;
		this.position = position;
	}

	public Spell(Player player, SpellTemplate template, byte position) {
		this.player = player;
		this.template = template;
		this.level = template.getLevels()[0]; // first level
		this.position = position;
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public void setId(Long pk) {
		this.id = pk;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the spell
	 */
	public SpellTemplate getTemplate() {
		return template;
	}

	/**
	 * @param template the spell to set
	 */
	public void setTemplate(SpellTemplate template) {
		this.template = template;
	}

	/**
	 * @return the level
	 */
	public SpellLevel getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(SpellLevel level) {
		this.level = level;
	}
	
	public void incrementLevel() {
		if (level.getId() >= SpellTemplate.MAX_LEVELS) return;
		level = template.getLevels()[level.getId()];
	}

	/**
	 * @return the position
	 */
	public byte getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(byte position) {
		this.position = position;
	}
	
	public BaseSpellType toBaseSpellType() {
		return new BaseSpellType(
				template.getId(),
				level.getId(),
				position >= 0 ?
						String.valueOf(StringUtils.ALPHABET.charAt(position)) :
						""
		);
	}

}
