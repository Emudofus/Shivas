package org.shivas.server.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.server.core.commands.types.PlayerType;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameClient;

public class KickCommand implements Command {
	
	private final RepositoryContainer repo;

	public KickCommand(RepositoryContainer repo) {
		this.repo = repo;
	}

	@Override
	public String name() {
		return "kick";
	}

	@Override
	public Conditions conditions() {
		Conditions conds = new Conditions();
		
		conds.add("target", new PlayerType(repo.players()), "The target");
		conds.add("message", Types.STRING, "The message");
		
		return conds;
	}

	@Override
	public String help() {
		return "Kick a player.";
	}

	@Override
	public boolean canUse(GameClient client) {
		return client.account().hasRights();
	}

	@Override
	public void use(GameClient client, DofusLogger log, Parameters params) {
		// TODO
	}

}
