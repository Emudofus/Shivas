package org.shivas.mods.example;

import org.shivas.common.params.Parameters;
import org.shivas.core.core.commands.Command;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.database.models.Account;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 01/11/12
 * Time: 18:09
 */
public class StaffCommand extends Command {
    @Inject
    private RepositoryContainer repos;

    @Override
    public String getName() {
        return "staff";
    }

    @Override
    public String getHelp() {
        return "Show online admins";
    }

    @Override
    public void use(GameClient gameClient, DofusLogger dofusLogger, Parameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("Online admins are :\n");

        for (Account account : repos.accounts()) {
            if (account.hasRights() && account.isConnected()) {
                Player admin = account.getCurrentPlayer();

                sb.append("  * ").append(admin.url()).append('\n');
            }
        }

        dofusLogger.info(sb.toString());
    }
}
