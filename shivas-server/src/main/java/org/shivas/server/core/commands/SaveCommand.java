package org.shivas.server.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameClient;

public class SaveCommand implements Command {
	
	private final RepositoryContainer repo;

	public SaveCommand(RepositoryContainer repo) {
		this.repo = repo;
	}

	@Override
	public String name() {
		return "save";
	}

	@Override
	public Conditions conditions() {
		return new Conditions();
	}

	@Override
	public String help() {
		return "Save all users' data";
	}

	@Override
	public boolean canUse(GameClient client) {
		return client.account().hasRights();
	}

	@Override
	public void use(GameClient client, DofusLogger log, Parameters params) {
		repo.save();
		
		log.info("All users' data has been saved.");
	}

}
