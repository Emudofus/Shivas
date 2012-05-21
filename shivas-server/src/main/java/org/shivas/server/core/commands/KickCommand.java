package org.shivas.server.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.server.core.channels.ChannelContainer;
import org.shivas.server.core.commands.types.PlayerType;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.game.GameClient;

public class KickCommand implements Command {
	
	private final RepositoryContainer repo;
	private final ChannelContainer channels;

	public KickCommand(RepositoryContainer repo, ChannelContainer channels) {
		this.repo = repo;
		this.channels = channels;
	}

	@Override
	public String name() {
		return "kick";
	}

	@Override
	public Conditions conditions() {
		Conditions conds = new Conditions();
		
		conds.add("target", new PlayerType(repo.players()), "The target");
		conds.add("msg", Types.STRING, "The message", true);
		
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
		Player target = params.get("target", Player.class);
		String message = params.get("msg", String.class);
		
		if (target.getClient() != null) {
			log.info("%s isn't connected", target.getName());
		} else {
			if (!message.isEmpty()) target.getClient().kick(message);
			else					target.getClient().kick();
			
			log.info("%s has been kicked", target.getName());
			
			channels.system().send(client.player(), String.format("%s has been kicked because : <i>%s</i>", target.url(), message != null ? message : "no reason"));
		}
	}

}
