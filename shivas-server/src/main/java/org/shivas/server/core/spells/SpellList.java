package org.shivas.server.core.spells;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.atomium.repository.PersistableEntityRepository;
import org.shivas.data.Container;
import org.shivas.data.entity.SpellTemplate;
import org.shivas.protocol.client.types.BaseSpellType;
import org.shivas.server.database.models.Player;
import org.shivas.server.database.models.Spell;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class SpellList implements Iterable<Spell> {

	private final Player owner;
	private final Container ctner;
	private final PersistableEntityRepository<Long, Spell> repo;
	
	private final Map<Short, Spell> spells = Maps.newHashMap();
	
	private short lastLevelUp;
	
	public SpellList(Player owner, Container ctner, PersistableEntityRepository<Long, Spell> repo) {
		this.owner = owner;
		this.ctner = ctner;
		this.repo = repo;
		this.lastLevelUp = owner.getExperience().level();
	}
	
	public void add(Spell spell) {
		spells.put(spell.getSpell().getId(), spell);
	}
	
	protected void persist(Spell spell) {
		repo.persistLater(spell);
		add(spell);
	}
	
	public Spell get(short tplId) {
		return spells.get(tplId);
	}
	
	public Spell get(SpellTemplate tpl) {
		return get(tpl.getId());
	}
	
	public void onLevelUp() {
		for (int i = lastLevelUp + 1; i <= owner.getExperience().level(); ++i) {
			SpellTemplate tpl = ctner.get(SpellTemplate.class).byId(i);
			if (tpl == null) continue;
			
			Spell spell = new Spell(owner, tpl);
			persist(spell);
		}
		
		lastLevelUp = owner.getExperience().level();
	}

	public Collection<BaseSpellType> toBaseSpellType() {
		return Collections2.transform(spells.values(), Converters.SPELL_TO_BASESPELLTYPE);
	}

	@Override
	public Iterator<Spell> iterator() {
		return spells.values().iterator();
	}
	
}
