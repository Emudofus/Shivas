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
		return Conditions.EMPTY;
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
	public void use(final GameClient client, final DofusLogger log, Parameters params) {
		new Thread(new Runnable() {
			public void run() {
				repo.save();
				log.info("Repositories has been saved");
			}
		}).run();
	}

}
