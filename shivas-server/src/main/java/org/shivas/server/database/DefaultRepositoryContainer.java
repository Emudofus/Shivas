package org.shivas.server.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.server.database.repositories.*;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	@Inject
	private AccountRepository accounts;
	
	@Inject
	private PlayerRepository players;
	
	@Inject
	private ExperienceTemplateRepository experienceTemplates;
	
	@Inject
	private BreedTemplateRepository breedTemplates;

	public AccountRepository accounts() {
		return accounts;
	}
	
	public PlayerRepository players() {
		return players;
	}

	public ExperienceTemplateRepository experienceTemplates() {
		return experienceTemplates;
	}

	public BreedTemplateRepository breedTemplates() {
		return breedTemplates;
	}

}
