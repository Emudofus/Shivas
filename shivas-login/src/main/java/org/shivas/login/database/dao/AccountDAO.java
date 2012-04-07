package org.shivas.login.database.dao;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.login.database.models.Account;

import com.google.inject.persist.Transactional;

@Singleton
public class AccountDAO {
	
	@Inject
	private EntityManager em;
	
	@Transactional
	public void create(Account account) {
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
		return em.createQuery("SELECT a FROM Account a WHERE a.id = :id", Account.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
	public Account findByName(String name) {
		return em.createQuery("SELECT a FROM Account a WHERE a.name = :name", Account.class)
				 .setParameter("name", name)
				 .getSingleResult();
	}
	
}
