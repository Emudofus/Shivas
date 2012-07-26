package org.shivas.server.database.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atomium.EntityManager;
import org.atomium.repository.BaseEntityRepository;
import org.atomium.repository.impl.AbstractEntityRepository;
import org.atomium.util.pk.LongPrimaryKeyGenerator;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilder;
import org.shivas.data.Container;
import org.shivas.data.entity.SpellLevel;
import org.shivas.data.entity.SpellTemplate;
import org.shivas.server.database.models.Player;
import org.shivas.server.database.models.Spell;

@Singleton
public class SpellRepository extends AbstractEntityRepository<Long, Spell> {
	
	private static final String TABLE_NAME = "spells";

	private final BaseEntityRepository<Integer, Player> players;
	private final Container ctner;
	
	private final QueryBuilder deleteQuery, persistQuery, saveQuery;
	private final Query loadQuery;

	@Inject
	protected SpellRepository(EntityManager em, BaseEntityRepository<Integer, Player> players, Container ctner) {
		super(em, new LongPrimaryKeyGenerator());
		this.players = players;
		this.ctner = ctner;
		
		this.deleteQuery = em.builder().delete(TABLE_NAME).where("id", Op.EQ);
		this.persistQuery = em.builder().insert(TABLE_NAME)
				.values("id", "player", "spell", "level", "position");
		this.saveQuery = em.builder().update(TABLE_NAME)
				.value("level").value("position")
				.where("id", Op.EQ);
		this.loadQuery = em.builder().select(TABLE_NAME).toQuery();
		
	}

	@Override
	protected Query buildDeleteQuery(Spell entity) {
		Query query = deleteQuery.toQuery();
		query.setParameter("id", entity.id());
		
		return query;
	}

	@Override
	protected Query buildPersistQuery(Spell entity) {
		Query query = persistQuery.toQuery();
		query.setParameter("id", entity.id());
		query.setParameter("player", entity.getPlayer().id());
		query.setParameter("spell", entity.getTemplate().getId());
		query.setParameter("level", entity.getLevel().getId());
		query.setParameter("position", entity.getPosition());
		
		return query;
	}

	@Override
	protected Query buildSaveQuery(Spell entity) {
		Query query = saveQuery.toQuery();
		query.setParameter("id", entity.id());
		query.setParameter("level", entity.getLevel().getId());
		query.setParameter("position", entity.getPosition());
		
		return query;
	}

	@Override
	protected Query buildLoadQuery() {
		return loadQuery;
	}

	@Override
	protected Spell load(ResultSet result) throws SQLException {
		SpellTemplate tpl = ctner.get(SpellTemplate.class).byId(result.getInt("spell"));
		SpellLevel level = tpl.getLevels()[result.getInt("level") - 1];
		
		Spell spell = new Spell(
				result.getLong("id"),
				players.find(result.getInt("player")),
				tpl,
				level,
				result.getByte("position")
		);
		
		spell.getPlayer().getSpells().add(spell);
		
		return spell;
	}

	@Override
	protected void unhandledException(Exception e) {
	}

}
