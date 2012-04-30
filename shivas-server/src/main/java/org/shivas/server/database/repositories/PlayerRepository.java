package org.shivas.server.database.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.shivas.data.Container;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.server.config.Config;
import org.shivas.server.core.Colors;
import org.shivas.server.core.experience.PlayerExperience;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;

@Singleton
public class PlayerRepository {

	@Inject
	private EntityManager em;
	
	@Inject
	private Config config;

	@Inject
	private Container ctner;

	public void persist(Player player) {
		em.getTransaction().begin();
		em.persist(player);
		em.getTransaction().commit();
	}

	public void remove(Player player) {
		em.getTransaction().begin();
		em.remove(player);
		em.getTransaction().commit();
	}

	public void update(Player player) {
		em.getTransaction().begin();
		em.merge(player);
		em.getTransaction().commit();
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
	
	public Player createDefault(Account owner, String name, int breed, Gender gender, int color1, int color2, int color3) {
		Player player = new Player(
				owner,
				name,
				ctner.get(Breed.class).byId(breed),
				gender,
				(short) (breed * 10 + gender.ordinal()),
				new Colors(color1, color2, color3),
				new PlayerExperience(ctner.get(Experience.class).byId(config.startLevel()))
		);
		owner.getPlayers().add(player);
		return player;
	}
	
}
