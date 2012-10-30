package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.core.core.guilds.GuildCreationInteraction;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 11:09
 */
public class CreateGuildCommand implements Command {
    @Override
    public String getName() {
        return "create_guild";
    }

    @Override
    public Conditions getConditions() {
        return Conditions.EMPTY;
    }

    @Override
    public String getHelp() {
        return "Open the creation guild panel";
    }

    @Override
    public boolean canUse(GameClient client) {
        return !client.player().hasGuild();
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        try {
            client.interactions().push(new GuildCreationInteraction(client)).begin();
        } catch (InteractionException e) {
            e.printStackTrace();
        }
    }
}
