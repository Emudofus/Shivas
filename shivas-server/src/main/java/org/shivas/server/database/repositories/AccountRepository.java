package org.shivas.server.database.repositories;

import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Sha1Cipher;
import org.shivas.server.database.models.Account;

@Singleton
public class AccountRepository {

	@Inject
	private EntityManager em;
	
	public void persist(Account account) {
		em.getTransaction().begin();
		em.persist(account);
		em.getTransaction().commit();
	}

	public void remove(Account account) {
		em.getTransaction().begin();
		em.remove(account);
		em.getTransaction().commit();
	}

	public void update(Account account) {
		em.getTransaction().begin();
		em.merge(account);
		em.getTransaction().commit();
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
