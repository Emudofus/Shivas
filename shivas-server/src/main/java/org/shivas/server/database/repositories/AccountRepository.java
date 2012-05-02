package org.shivas.server.database.repositories;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atomium.EntityManager;
import org.atomium.repository.impl.AbstractLiveEntityRepository;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.SelectQueryBuilder;
import org.atomium.util.query.UpdateQueryBuilder;
import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Sha1Cipher;
import org.shivas.server.database.models.Account;

@Singleton
public class AccountRepository extends AbstractLiveEntityRepository<Integer, Account> {
	
	public static final String TABLE_NAME = "accounts";
	
	private SelectQueryBuilder loadByIdQuery, loadByNameQuery;
	private UpdateQueryBuilder saveQuery;

	@Inject
	public AccountRepository(EntityManager em) {
		super(em);
		
		this.loadByIdQuery = em.builder().select(TABLE_NAME).where("id", Op.EQ);
		this.loadByNameQuery = em.builder().select(TABLE_NAME).where("name", Op.EQ);
		this.saveQuery = em.builder().update(TABLE_NAME).value("TODO").where("id", Op.EQ); // TODO
	}
	
	public Cipher passwordCipher() {
		try {
			return new Sha1Cipher();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); // MUST NOT HAPPEN
			return null;
		}
	}
	
	public Account find(String name) {
		Query query = loadByNameQuery.toQuery();
		query.setParameter("name", name);
		
		return find(query);
	}

	@Override
	protected Query buildLoadQuery(Integer pk) {
		Query query = loadByIdQuery.toQuery();
		query.setParameter("id", pk);
		
		return query;
	}

	@Override
	protected Query buildSaveQuery(Account entity) {
		Query query = saveQuery.toQuery();
		query.setParameter("id", entity.id());
		//TODO
		
		return query;
	}

	@Override
	protected Account load(ResultSet result) {
		return new Account(); // TODO
	}
	
}
