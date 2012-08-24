package org.shivas.server.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.core.services.NetworkStatisticsCenter;
import org.shivas.server.services.game.GameClient;

public class AverageHandleCommand implements Command {
	
	private final NetworkStatisticsCenter stats;

	public AverageHandleCommand(NetworkStatisticsCenter stats) {
		this.stats = stats;
	}

	@Override
	public String name() {
		return "avg";
	}

	@Override
	public Conditions conditions() {
		Conditions conds = new Conditions();
		conds.add("h", Types.STRING, "Packet's header");
		
		return conds;
	}

	@Override
	public String help() {
		return "Returns the average of handle duration";
	}

	@Override
	public boolean canUse(GameClient client) {
		return true;
	}

	@Override
	public void use(GameClient client, DofusLogger log, Parameters params) {
		String header = params.get("h", String.class);
		
		log.info("It takes %d ms to handle \"%s\" packet", stats.get(header), header);
	}

}
