package org.shivas.core.core.commands;

import org.shivas.common.params.Parameters;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public class GcCommand extends Command {
    @Override
    public String getName() {
        return "gc";
    }

    @Override
    public String getHelp() {
        return "Trigger a full garbage collector scan";
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        Runtime rt = Runtime.getRuntime();

        double before = ((double) rt.totalMemory() - rt.freeMemory()) / 1_000_000;
        log.info("Before: %.2fM", before);

        System.runFinalization();
        System.gc();

        double after = ((double) rt.totalMemory() - rt.freeMemory()) / 1_000_000;
        log.info("After: %.2fM (%.2fM)", after, after - before);
    }
}
