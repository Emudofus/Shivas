package org.shivas.server.database.repositories;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.server.database.annotations.DefaultDatabase;
import org.shivas.server.database.models.Account;

import com.google.inject.persist.Transactional;

@Singleton
public class AccountRepository {

	@Inject 
	@DefaultDatabase
	private EntityManager em;
	
	@Transactional
	public void persist(Account account) {
		em.persist(account);
	}

	@Transactional
	public void remove(Account account) {
		em.remove(account);
	}

	@Transactional
	public void update(Account account) {
		em.merge(account);
	}
	
	public Account findById(int id) {
		return em.createQuery("from Account a where a.id = :id", Account.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
	public Account findByName(String name) {
		return em.createQuery("from Account a where a.name = :name", Account.class)
				 .setParameter("name", name)
				 .getSingleResult();
	}
	
}
