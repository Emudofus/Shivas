package org.shivas.mods.example;

import org.shivas.common.params.Parameters;
import org.shivas.core.core.commands.Command;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 21:24
 */
public class MyExampleCommand extends Command {
    @Override
    public String getName() {
        return "example";
    }

    @Override
    public String getHelp() {
        return "Some kind of example command";
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        log.info("This is a example");
        log.info("Hello %s !", client.player().url());
    }
}
