package org.shivas.server.database.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.atomium.EntityManager;
import org.atomium.repository.impl.AbstractEntityRepository;
import org.atomium.util.pk.LongPrimaryKeyGenerator;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilder;
import org.shivas.server.database.models.GameItem;

public class GameItemRepository extends AbstractEntityRepository<Long, GameItem> {
	
	private final QueryBuilder delete, persist, save;
	private final Query load;

	public GameItemRepository(EntityManager em) {
		super(em, new LongPrimaryKeyGenerator());
		
		delete = em.builder().delete("items").where("id", Op.EQ);
		persist = em.builder().insert("items").values("id"); // TODO items' insert query
		save = em.builder().update("items").where("id", Op.EQ); // TODO items' update query
		load = em.builder().select("items").toQuery();
	}

	@Override
	protected Query buildDeleteQuery(GameItem entity) {
		Query query = delete.toQuery();
		query.setParameter("id", entity.id());
		
		return query;
	}

	@Override
	protected Query buildPersistQuery(GameItem entity) {
		Query query = persist.toQuery();
		query.setParameter("id", entity.id());
		
		return query;
	}

	@Override
	protected Query buildSaveQuery(GameItem entity) {
		Query query = save.toQuery();
		query.setParameter("id", entity.id());
		
		return query;
	}

	@Override
	protected Query buildLoadQuery() {
		return load;
	}

	@Override
	protected GameItem load(ResultSet result) throws SQLException {
		return new GameItem(); // TODO load item
	}

	@Override
	protected void unhandledException(Exception e) {
	}

}
