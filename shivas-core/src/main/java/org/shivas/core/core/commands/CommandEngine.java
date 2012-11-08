package org.shivas.core.core.commands;

import com.google.common.collect.Maps;
import org.shivas.common.params.GnuParser;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.ParametersParser;
import org.shivas.common.params.ParsingException;
import org.shivas.core.config.InjectConfig;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.services.game.GameClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Singleton
public class CommandEngine {

	private final Map<String, Command> commands = Maps.newHashMap();
	private final ParametersParser parser = new GnuParser();

    @InjectConfig(key="commands.enabled")
    private boolean enabled;

    @InjectConfig(key="commands.prefix")
    private String prefix;

    @Inject
    public void init(Set<Command> commands) {
        for (Command command : commands) {
            add(command);
        }
    }

    public void add(Command command) {
        commands.put(command.getName(), command);
    }

    public boolean canHandle(String input) {
        return enabled && input.startsWith(prefix);
    }

    public ParametersParser getParser() {
        return parser;
    }

    public Collection<Command> getCommands() {
        return commands.values();
    }
	
	public void use(GameClient client, DofusLogger log, String command) {
        if (!enabled) return;

        if (command.startsWith(prefix)) command = command.substring(prefix.length());

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
