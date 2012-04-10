package org.shivas.game.database.repositories;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.game.database.models.Breed;

@Singleton
public class BreedRepository {
	
	@Inject
	private EntityManager em;
	
	public Breed findById(short id) {
		return em.createQuery("SELECT b FROM Breed b WHERE b.id = :id", Breed.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
}
