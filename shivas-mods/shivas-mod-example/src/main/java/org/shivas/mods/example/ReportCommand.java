package org.shivas.mods.example;

import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.Types;
import org.shivas.core.core.commands.Command;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.database.models.Account;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/11/12
 * Time: 17:54
 */
public class ReportCommand extends Command {
    @Inject
    private RepositoryContainer repos;

    @Override
    public String getName() {
        return "report";
    }

    @Override
    public String getHelp() {
        return "Send a message to all connected admins";
    }

    @Override
    public Conditions getConditions() {
        return new Conditions() {{
            add("msg", Types.STRING, "the message to send");
        }};
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        String message = params.get("msg", String.class);

        for (Account account : repos.accounts()) {
            if (account.hasRights() && account.isConnected()) {
                GameClient admin = account.getCurrentPlayer().getClient();
                admin.tchat().log("<font color=\"#000000\"><b>REPORT</b></font> from %s : \"<i>%s</i>\"", client.player().url(), message);
            }
        }

        log.info("Your message has been sent to connected admins");
    }
}
