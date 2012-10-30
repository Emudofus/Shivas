package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.core.core.channels.ChannelContainer;
import org.shivas.core.core.commands.types.PlayerType;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

public class KickCommand implements Command {

    @Inject
	private RepositoryContainer repo;

    @Inject
	private ChannelContainer channels;

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public Conditions getConditions() {
		return new Conditions() {{
            add("target", new PlayerType(repo.players()), "The target");
            add("msg", Types.STRING, "The message", true);
        }};
	}

	@Override
	public String getHelp() {
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
		
		if (target.getClient() == null) {
			log.info("%s isn't connected", target.getName());
		} else {
			if (!message.isEmpty()) target.getClient().kick(message);
			else					target.getClient().kick();
			
			log.info("%s has been kicked", target.getName());
			
			channels.system().send(client.player(), String.format("%s has been kicked because : <i>%s</i>", target.url(), message != null ? message : "no reason"));
		}
	}

}
