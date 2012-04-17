package org.shivas.server.database.repositories;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.shivas.server.config.Config;
import org.shivas.server.core.experience.PlayerExperience;
import org.shivas.server.database.annotations.StaticDatabase;
import org.shivas.server.database.models.ExperienceTemplate;

import com.google.common.collect.Maps;

@Singleton
public class ExperienceTemplateRepository {
	
	private Map<Short, ExperienceTemplate> entities = Maps.newHashMap();
	
	private ExperienceTemplate startExperienceTemplate;
	
	@Inject
	public ExperienceTemplateRepository(@StaticDatabase EntityManager em, Config config) {
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
		
		this.startExperienceTemplate = entities.get(config.startLevel());
	}
	
	public ExperienceTemplate findByLevel(short level) {
		return entities.get(level);
	}
	
	public PlayerExperience newStartPlayerExperience() {
		return new PlayerExperience(startExperienceTemplate);
	}

}
