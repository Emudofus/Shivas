package org.shivas.core.core.commands;

import org.atomium.repository.BaseEntityRepository;
import org.shivas.common.params.Conditions;
import org.shivas.common.params.Parameters;
import org.shivas.core.core.commands.types.PlayerType;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 04/11/12
 * Time: 17:17
 */
public class TeleportCommand extends Command {
    @Inject
    private BaseEntityRepository<Integer, Player> players;

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getHelp() {
        return "Teleport a player to another";
    }

    @Override
    public Conditions getConditions() {
        return new Conditions(){{
            add("from", new PlayerType(players), "The player to teleport");
            add("to", new PlayerType(players), "The player that will receive the teleported one", true);
        }};
    }

    @Override
    public boolean canUse(GameClient client) {
        return client.account().hasRights();
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        Player from = params.get("from", Player.class),
               to   = params.get("to",   Player.class);

        if (to == null) {
            to = client.player();
        }

        if (from.getClient() == null || from.getClient().isBusy()) {
            log.error("%s is not online or busy", from.getName());
        } else if (to.getClient() == null || to.getClient().isBusy()) {
            log.error("%s is not online or busy", to.getName());
        } else {
            from.teleport(to.getLocation().getMap(), to.getLocation().getCell());

            from.getClient().tchat().info("You've been teleported to %s.", to.url());
            if (to == client.player()) {
                log.info("You've successfully teleported %s to you.", from.url());
            } else {
                to.getClient().tchat().info("%s has been teleported to you.", from.url());
                log.info("%s has been teleported to %s.", from.url(), to.url());
            }
        }
    }
}
