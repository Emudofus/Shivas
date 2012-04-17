package org.shivas.server.database;

import org.shivas.server.database.repositories.*;

public interface RepositoryContainer {
	AccountRepository accounts();
	PlayerRepository players();
	
	ExperienceTemplateRepository experienceTemplates();
}
