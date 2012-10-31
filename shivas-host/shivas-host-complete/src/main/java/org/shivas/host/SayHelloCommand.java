package org.shivas.host;

import org.shivas.common.params.Parameters;
import org.shivas.core.core.commands.Command;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 19:59
 */
public class SayHelloCommand extends Command {
    @Override
    public String getName() {
        return "say_hello";
    }

    @Override
    public String getHelp() {
        return "Say hello!";
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        log.info("Hello %s!", client.player().url());
    }
}
