package org.shivas.core.core.commands;

import org.shivas.common.params.Parameters;
import org.shivas.common.params.ParametersParser;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 04/11/12
 * Time: 17:34
 */
public class HelpCommand extends Command {
    @Inject CommandEngine engine;

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Show commands";
    }

    @Override
    public void use(GameClient client, DofusLogger log, Parameters params) {
        ParametersParser parser = engine.getParser();
        StringBuilder sb = new StringBuilder();

        for (Command command : engine.getCommands()) {
            if (!command.canUse(client)) continue;

            sb.append("<b>").append(command.getName()).append("</b>");
            sb.append(" => <i>").append(command.getHelp()).append("</i>\n");
            sb.append(parser.buildHelpMessage(command.getConditions()));
            sb.append("\n");
        }

        log.log(sb.toString());
    }
}
