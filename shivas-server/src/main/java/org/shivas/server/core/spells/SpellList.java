package org.shivas.server.core.spells;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.atomium.repository.PersistableEntityRepository;
import org.shivas.data.entity.SpellBreed;
import org.shivas.data.entity.SpellTemplate;
import org.shivas.protocol.client.types.BaseSpellType;
import org.shivas.server.database.models.Player;
import org.shivas.server.database.models.Spell;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class SpellList implements Iterable<Spell> {

	private final Player owner;
	private final PersistableEntityRepository<Long, Spell> repo;
	
	private final Map<Short, Spell> spells = Maps.newHashMap();
	
	public SpellList(Player owner, PersistableEntityRepository<Long, Spell> repo) {
		this.owner = owner;
		this.repo = repo;
	}
	
	public SpellList onCreated() {
		for (SpellBreed spellbreed : owner.getBreed().getSpells().values()) {
			if (spellbreed.getMinLevel() <= owner.getExperience().level()) {
				persist(new Spell(
						owner,
						spellbreed.getTemplate(),
						(byte) spellbreed.getPosition()
				));
			}
		}
		return this;
	}
	
	public Player getOwner() {
		return owner;
	}

	public void add(Spell spell) {
		spells.put(spell.getTemplate().getId(), spell);
	}
	
	public void persist(Spell spell) {
		repo.persistLater(spell);
		add(spell);
	}
	
	public Spell get(short tplId) {
		return spells.get(tplId);
	}
	
	public Spell get(SpellTemplate tpl) {
		return get(tpl.getId());
	}

	public Collection<BaseSpellType> toBaseSpellType() {
		return Collections2.transform(spells.values(), Converters.SPELL_TO_BASESPELLTYPE);
	}

	@Override
	public Iterator<Spell> iterator() {
		return spells.values().iterator();
	}
	
}
