package org.shivas.login.database.repositories;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.common.crypto.Cipher;
import org.shivas.login.database.models.Account;

import com.google.inject.persist.Transactional;

@Singleton
public class AccountRepository {
	
	@Inject
	private EntityManager em;
	
	@Inject @Named(value="account.password.cipher")
	private Cipher passwordCipher;
	
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
	
	public Cipher getPasswordCipher() {
		return passwordCipher;
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
