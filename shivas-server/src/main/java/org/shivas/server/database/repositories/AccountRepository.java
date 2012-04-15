package org.shivas.server.database.repositories;

import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Sha1Cipher;
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
	
	public Cipher passwordCipher() {
		try {
			return new Sha1Cipher();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); // MUST NOT HAPPEN
			return null;
		}
	}
	
	public Account findById(int id) throws NoResultException {
		return em.createQuery("from Account a where a.id = :id", Account.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
	public Account findByName(String name) throws NoResultException {
		return em.createQuery("from Account a where a.name = :name", Account.class)
				 .setParameter("name", name)
				 .getSingleResult();
	}
	
}
