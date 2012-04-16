package org.shivas.server.database.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.shivas.server.core.Colors;
import org.shivas.server.core.experience.PlayerExperience;
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
	
	public Player findById(int id) throws NoResultException {
		return em.createQuery("from Player p where p.id = :id", Player.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
	public Player findByName(String name) throws NoResultException {
		return em.createQuery("from Player p where p.name = :name", Player.class)
				 .setParameter("name", name)
				 .getSingleResult();
	}
	
	public List<Player> findByOwner(Account owner) throws NoResultException {
		return em.createQuery("from Player p where p.owner = :owner", Player.class)
				 .setParameter("owner", owner)
				 .getResultList();
	}
	
	public Player createDefault(Account owner, String name, int breed, boolean gender, int color1, int color2, int color3) {
		return new Player(
				owner,
				name,
				(short) (breed * 10 + (gender ? 1 : 0)),
				new Colors(color1, color2, color3),
				new PlayerExperience()
		);
	}
	
}
