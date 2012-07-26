package org.shivas.server.core.spells;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.atomium.repository.EntityRepository;
import org.shivas.data.entity.SpellTemplate;
import org.shivas.protocol.client.types.BaseSpellType;
import org.shivas.server.database.models.Player;
import org.shivas.server.database.models.Spell;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class SpellList implements Iterable<Spell> {

	public static final byte DEFAULT_POSITION = -1;
	
	private final Player owner;
	private final EntityRepository<Long, Spell> repo;
	
	private final Map<Short, Spell> spells = Maps.newHashMap();
	
	public SpellList(Player owner, EntityRepository<Long, Spell> repo) {
		this.owner = owner;
		this.repo = repo;
	}
	
	public Player getOwner() {
		return owner;
	}

	public void add(Spell spell) {
		spells.put(spell.getTemplate().getId(), spell);
	}
	
	public void persist(Spell spell) {
		add(spell);
		repo.persistLater(spell);
	}
	
	public Spell get(short tplId) {
		return spells.get(tplId);
	}
	
	public Spell get(SpellTemplate tpl) {
		return get(tpl.getId());
	}
	
	public Spell get(byte position) {
		for (Spell spell : spells.values()) {
			if (spell.getPosition() == position)
				return spell;
		}
		return null;
	}
	
	public void move(Spell spell, byte position) {
		Spell samePos; // si la place est déjà prise, on laisse la place au nouveau sort
		if (position != -1 && (samePos = get(position)) != null) {
			samePos.setPosition(DEFAULT_POSITION);
			repo.saveLater(samePos);
		}
		
		spell.setPosition(position);
		repo.saveLater(spell);
	}

	public Collection<BaseSpellType> toBaseSpellType() {
		return Collections2.transform(spells.values(), Converters.SPELL_TO_BASESPELLTYPE);
	}

	@Override
	public Iterator<Spell> iterator() {
		return spells.values().iterator();
	}
	
}
