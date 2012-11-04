package org.shivas.core.core.commands;

import com.google.common.collect.Maps;
import org.shivas.common.params.GnuParser;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.ParametersParser;
import org.shivas.common.params.ParsingException;
import org.shivas.core.config.Config;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;

@Singleton
public class CommandEngine {

	private final Map<String, Command> commands = Maps.newHashMap();
	private final ParametersParser parser = new GnuParser();

    private Config config;

    @Inject
    public void init(Config config, Set<Command> commands) {
        this.config = config;

        for (Command command : commands) {
            add(command);
        }
    }

    public void add(Command command) {
        commands.put(command.getName(), command);
    }

    public boolean canHandle(String input) {
        return config.cmdEnabled() &&
               input.startsWith(config.cmdPrefix());
    }
	
	public void use(GameClient client, DofusLogger log, String command) {
        command = command.substring(config.cmdPrefix().length());

		int index = command.indexOf(" ");
		String name;
		if (index >= 0) {
			name = command.substring(0, index);
			command = command.substring(index);
		} else {
			name = command;
			command = "";
		}
		
		Command cmd = commands.get(name);
		if (cmd == null || !cmd.canUse(client)) {
			log.error("unknown command \"%s\"", name);
		} else {
			try {
				Parameters params = parser.parse(command, cmd.getConditions());
				cmd.use(client, log, params);
			} catch (ParsingException e) {
				log.error(e.getMessage());
			}
		}
	}
}
