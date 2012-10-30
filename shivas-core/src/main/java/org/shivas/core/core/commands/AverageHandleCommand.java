package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.core.services.NetworkStatisticsCenter;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

public class AverageHandleCommand implements Command {

    @Inject
	private NetworkStatisticsCenter stats;

	@Override
	public String getName() {
		return "avg";
	}

	@Override
	public Conditions getConditions() {
		return new Conditions() {{
            add("h", Types.STRING, "Packet's header");
        }};
	}

	@Override
	public String getHelp() {
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
