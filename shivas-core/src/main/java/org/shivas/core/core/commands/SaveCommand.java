package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

public class SaveCommand implements Command {

    @Inject
	private RepositoryContainer repo;

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public Conditions getConditions() {
		return Conditions.EMPTY;
	}

	@Override
	public String getHelp() {
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
