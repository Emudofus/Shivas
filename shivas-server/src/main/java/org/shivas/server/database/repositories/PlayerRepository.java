package org.shivas.server.database.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.server.database.annotations.DefaultDatabase;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;

import com.google.inject.persist.Transactional;

@Singleton
public class PlayerRepository {

	@Inject
	@DefaultDatabase
	private EntityManager em;
	
	@Transactional
	public void persist(Player player) {
		em.persist(player);
	}

	@Transactional
	public void remove(Player player) {
		em.remove(player);
	}

	@Transactional
	public void update(Player player) {
		em.merge(player);
	}
	
	public Player findById(int id) {
		return em.createQuery("from Player p where p.id = :id", Player.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
	public Player findByName(String name) {
		return em.createQuery("from Player p where p.name = :name", Player.class)
				 .setParameter("name", name)
				 .getSingleResult();
	}
	
	public List<Player> findByOwner(Account owner) {
		return em.createQuery("from Player p where p.owner = :owner", Player.class)
				 .setParameter("owner", owner)
				 .getResultList();
	}
	
}