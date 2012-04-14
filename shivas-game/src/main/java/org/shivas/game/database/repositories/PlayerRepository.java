package org.shivas.game.database.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.game.database.models.Player;

import com.google.inject.persist.Transactional;

@Singleton
public class PlayerRepository {

	@Inject
	private EntityManager em;
	
	@Transactional
	public void create(Player player) {
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
		return em.createQuery("SELECT p FROM Player p WHERE p.id = :id", Player.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
	public List<Player> findByOwner(int owner) {
		return em.createQuery("SELECT p FROM Player p WHERE p.owner = :owner", Player.class)
				 .setParameter("owner", owner)
				 .getResultList();
	}
	
	public Integer countByOwner(int owner) {
		return findByOwner(owner).size();
	}
	
	public Player findByName(String name) {
		return em.createQuery("SELECT p FROM Player p WHERE p.name = :name", Player.class)
				 .setParameter("name", name)
				 .getSingleResult();
	}
	
}
