package org.shivas.core.core.commands;

import org.shivas.common.params.Parameters;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public class StatsCommand extends Command {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getHelp() {
        return "Display engine's technical statistics";
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        Runtime rt = Runtime.getRuntime();

        log.info("Available processors: %d", rt.availableProcessors());
        log.info("Used memory: %.2fM", ((double) rt.totalMemory() - rt.freeMemory())/1_000_000);
        log.info("Pre-allocated memory: %.2fM", (double) rt.totalMemory()/1_000_000);
    }
}
