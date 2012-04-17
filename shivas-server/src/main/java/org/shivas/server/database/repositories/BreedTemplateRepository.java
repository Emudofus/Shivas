package org.shivas.server.database.repositories;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.server.database.annotations.StaticDatabase;
import org.shivas.server.database.models.BreedTemplate;

@Singleton
public class BreedTemplateRepository {

	@Inject
	@StaticDatabase
	private EntityManager em;
	
	public BreedTemplate findById(int id) {
		return em.createQuery("from BreedTemplate b where b.id = :id", BreedTemplate.class)
				 .setParameter("id", id)
				 .getSingleResult();
	}
	
}
