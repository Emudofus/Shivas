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
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Contact;

@Singleton
public class ContactRepository extends AbstractEntityRepository<Long, Contact> {
	
	public static final String TABLE_NAME = "contacts";
	
	private final QueryBuilder persist, save, delete;
	private final Query load;
	
	private final BaseEntityRepository<Integer, Account> accounts;

	@Inject
	public ContactRepository(EntityManager em, BaseEntityRepository<Integer, Account> accounts) {
		super(em, new LongPrimaryKeyGenerator());
		
		this.persist = em.builder().insert(TABLE_NAME).values("id", "owner", "target", "type");
		this.save 	 = em.builder().update(TABLE_NAME).value("owner").value("target").value("type").where("id", Op.EQ);
		this.delete  = em.builder().delete(TABLE_NAME).where("id", Op.EQ);
		this.load 	 = em.builder().select(TABLE_NAME).toQuery();
		
		this.accounts = accounts;
	}

	@Override
	protected Query buildDeleteQuery(Contact entity) {
		Query query = delete.toQuery();
		query.setParameter("id", entity.id());
		
		return query;
	}

	@Override
	protected Query buildPersistQuery(Contact entity) {
		Query query = persist.toQuery();
		query.setParameter("id", entity.id());
		query.setParameter("owner", entity.getOwnerReference().getPk());
		query.setParameter("target", entity.getTargetReference().getPk());
		query.setParameter("type", entity.getType().ordinal());
		
		return query;
	}

	@Override
	protected Query buildSaveQuery(Contact entity) {
		Query query = save.toQuery();
		query.setParameter("id", entity.id());
		query.setParameter("owner", entity.getOwnerReference().getPk());
		query.setParameter("target", entity.getTargetReference().getPk());
		query.setParameter("type", entity.getType().ordinal());
		
		return query;
	}

	@Override
	protected Query buildLoadQuery() {
		return load;
	}

	@Override
	protected Contact load(ResultSet result) throws SQLException {
		Contact entity = new Contact();
		entity.setId(result.getLong("id"));
		entity.setOwnerReference(accounts.getLazyReference(result.getInt("owner")));
		entity.setTargetReference(accounts.getLazyReference(result.getInt("target")));
		entity.setType(Contact.Type.valueOf(result.getInt("type")));
		
		return entity;
	}

	@Override
	protected void unhandledException(Exception e) {
	}

}
