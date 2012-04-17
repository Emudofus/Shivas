package org.shivas.server.database.repositories;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.server.database.annotations.StaticDatabase;
import org.shivas.server.database.models.ExperienceTemplate;

import com.google.common.collect.Maps;

@Singleton
public class ExperienceTemplateRepository {
	
	@Inject
	@StaticDatabase
	private EntityManager em;
	
	private Map<Short, ExperienceTemplate> entities = Maps.newHashMap();
	
	public void init() {
		List<ExperienceTemplate> result = em
				.createQuery("from ExperienceTemplate e", ExperienceTemplate.class)
				.getResultList();
		
		for (int i = 0; i < result.size(); ++i) {
			ExperienceTemplate previous = i > 0 ? result.get(i - 1) : null,
					           next     = i < result.size() - 1 ? result.get(i + 1) : null,
					           current  = result.get(i);
			
			current.setPrevious(previous);
			current.setNext(next);
			
			entities.put(current.getLevel(), current);
		}
	}
	
	public ExperienceTemplate findByLevel(short level) {
		return entities.get(level);
	}

}
