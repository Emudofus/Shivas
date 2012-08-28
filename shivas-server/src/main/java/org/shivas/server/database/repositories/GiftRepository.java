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
import org.shivas.data.entity.ItemTemplate;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Gift;

@Singleton
public class GiftRepository extends AbstractEntityRepository<Long, Gift> {
	
	private final BaseEntityRepository<Integer, Account> accounts;
	private final Container ctner;
	
	private final Query loadQuery;
	private final QueryBuilder deleteQuery, persistQuery, saveQuery;

	@Inject
	public GiftRepository(EntityManager em, BaseEntityRepository<Integer, Account> accounts, Container ctner) {
		super(em, new LongPrimaryKeyGenerator());
		
		this.accounts = accounts;
		this.ctner = ctner;
		
		this.loadQuery = em.builder().select("gifts").toQuery();
		this.deleteQuery = em.builder()
				.delete("gifts")
				.where("id", Op.EQ);
		this.persistQuery = em.builder()
				.insert("gifts")
				.values("id", "owner_id", "item", "quantity", "title", "message");
		this.saveQuery = em.builder()
				.update("gifts")
				.value("owner_id").value("item").value("quantity").value("title").value("message");
	}

	@Override
	protected Query buildLoadQuery() {
		return loadQuery;
	}

	@Override
	protected Query buildDeleteQuery(Gift arg0) {
		return deleteQuery.toQuery()
				.setParameter("id", arg0.getId());
	}
	
	private Query bindValues(QueryBuilder qb, Gift gift) {
		return qb.toQuery()
				.setParameter("owner_id", gift.getOwner().getId())
				.setParameter("item", gift.getItem().getId())
				.setParameter("quantity", gift.getQuantity())
				.setParameter("title", gift.getTitle())
				.setParameter("message", gift.getMessage());
	}

	@Override
	protected Query buildPersistQuery(Gift arg0) {
		return bindValues(persistQuery, arg0);
	}

	@Override
	protected Query buildSaveQuery(Gift arg0) {
		return bindValues(saveQuery, arg0);
	}

	@Override
	protected Gift load(ResultSet rset) throws SQLException {
		Gift gift = new Gift();
		gift.setId(rset.getLong("id"));
		gift.setOwner(accounts.find(rset.getInt("owner")));
		gift.setItem(ctner.get(ItemTemplate.class).byId(rset.getInt("item")));
		gift.setQuantity(rset.getInt("quantity"));
		gift.setTitle(rset.getString("title"));
		gift.setMessage(rset.getString("message"));
		
		return gift;
	}

	@Override
	protected void unhandledException(Exception arg0) {
	}

}
