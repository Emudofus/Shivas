package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

public abstract class Command {
	public abstract String getName();
    public abstract String getHelp();

	public abstract void use(GameClient client, DofusLogger log, Parameters params);

    public Conditions getConditions() {
        return Conditions.EMPTY;
    }

    public boolean canUse(GameClient client) {
        return true;
    }
}
