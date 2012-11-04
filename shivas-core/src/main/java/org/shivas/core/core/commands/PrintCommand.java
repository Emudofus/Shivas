package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 04/11/12
 * Time: 15:53
 */
public class PrintCommand extends Command {
    @Override
    public String getName() {
        return "print";
    }

    @Override
    public String getHelp() {
        return "Show a message in the console";
    }

    @Override
    public boolean canUse(GameClient client) {
        return client.account().hasRights();
    }

    @Override
    public Conditions getConditions() {
        return new Conditions(){{
            add("msg", Types.STRING, "the message to show");
            add("color", Types.STRING, "the message's color", true);
        }};
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        String msg = params.get("msg", String.class);
        String color = params.has("color") ? params.get("color", String.class) : null;

        if (color != null) {
            msg = String.format("<font color=\"%s\">%s</font>", color, msg);
        }

        log.log(msg);
    }
}
