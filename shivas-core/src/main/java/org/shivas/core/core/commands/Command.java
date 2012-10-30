package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

public interface Command {

	String getName();
	Conditions getConditions();
	String getHelp();
	
	boolean canUse(GameClient client);
	void use(GameClient client, DofusLogger log, Parameters params);
	
}
