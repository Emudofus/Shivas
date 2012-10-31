package org.shivas.core.database.models;

import org.atomium.PersistableEntity;
import org.shivas.common.StringUtils;
import org.shivas.common.maths.Range;
import org.shivas.core.core.castables.Castable;
import org.shivas.core.core.castables.effects.EffectInterface;
import org.shivas.data.entity.SpellLevel;
import org.shivas.data.entity.SpellTemplate;
import org.shivas.protocol.client.types.BaseSpellType;

import java.io.Serializable;
import java.util.Collection;

import static org.shivas.common.collections.CollectionQuery.from;

public class Spell implements Serializable, PersistableEntity<Long>, Castable {

	private static final long serialVersionUID = 4763070969120891187L;
	
	private long id;
	private Player player;
	private SpellTemplate template;
	private SpellLevel level;
	private byte position;
    private Collection<EffectInterface> effects, criticalEffects;
	
	public Spell() { }

	public Spell(long id, Player player, SpellTemplate template, SpellLevel level, byte position) {
		this.id = id;
		this.player = player;
		this.template = template;
		this.level = level;
		this.position = position;
        initEffects();
	}

	public Spell(Player player, SpellTemplate template, byte position) {
		this.player = player;
		this.template = template;
		this.level = template.getLevels()[0]; // first level
		this.position = position;
        initEffects();
	}

	@Override
	public Long getId() {
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
	
	public void incrementLevel() {
		if (level.getId() >= SpellTemplate.MAX_LEVELS) return;
		level = template.getLevels()[level.getId()];

        initEffects();
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

    @Override
    public short getCost() {
        return level.getCostAP();
    }

    @Override
    public short getCriticalRate() {
        return level.getCriticalRate();
    }

    @Override
    public short getFailureRate() {
        return level.getFailureRate();
    }

    @Override
    public Range getRange() {
        return new Range(level.getMinRange(), level.getMaxRange());
    }

    private void initEffects() {
        effects = from(level.getEffects()).ofType(EffectInterface.class).computeList();
        criticalEffects = from(level.getCriticalEffects()).ofType(EffectInterface.class).computeList();
    }

    @Override
    public Collection<EffectInterface> getEffects(boolean critical) {
        return !critical ?
                effects :
                criticalEffects;
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
