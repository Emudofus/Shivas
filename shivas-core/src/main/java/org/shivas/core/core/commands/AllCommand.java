package org.shivas.core.core.commands;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.database.models.Account;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 04/11/12
 * Time: 17:09
 */
public class AllCommand extends Command {
    @Inject
    private RepositoryContainer repos;

    @Override
    public String getName() {
        return "all";
    }

    @Override
    public String getHelp() {
        return "Sends a message to all online players";
    }

    @Override
    public Conditions getConditions() {
        return new Conditions(){{
            add("msg", Types.STRING, "The message to send");
            add("anonymous", Types.BOOLEAN, "Show your pseudo if true", true);
        }};
    }

    @Override
    public boolean canUse(GameClient client) {
        return client.account().hasRights();
    }

    @Override
    public void use(GameClient c, DofusLogger log, Parameters params) {
        String msg = params.get("msg", String.class);
        boolean anonymous = params.get("anonymous", false);
        if (anonymous) {
            msg = String.format("[<i>ADMIN</i>] %s : %s", c.player().url(), msg);
        }

        for (Account account : repos.accounts()) {
            if (account.isConnected()) {
                GameClient client = account.getCurrentPlayer().getClient();
                client.tchat().log(msg);
            }
        }
    }
}
