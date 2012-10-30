package org.shivas.server.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.services.game.GameClient;

public interface Command {

	String getName();
	Conditions getConditions();
	String getHelp();
	
	boolean canUse(GameClient client);
	void use(GameClient client, DofusLogger log, Parameters params);
	
}
