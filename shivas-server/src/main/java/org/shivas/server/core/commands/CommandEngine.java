package org.shivas.server.core.commands;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.common.params.GnuParser;
import org.shivas.common.params.Parameters;
import org.shivas.common.params.ParametersParser;
import org.shivas.common.params.ParsingException;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameClient;

import com.google.common.collect.Maps;

@Singleton
public class CommandEngine {

	private final Map<String, Command> commands = Maps.newHashMap();
	private final ParametersParser parser = new GnuParser();

	@Inject
	public CommandEngine(RepositoryContainer repo) {
		add(new KickCommand(repo));
		add(new SaveCommand(repo));
	}
	
	protected void add(Command command) {
		commands.put(command.name(), command);
	}
	
	public void use(GameClient client, DofusLogger log, String command) {
		int index = command.indexOf(" ");
		String name = command.substring(0, index);
		command = command.substring(index);
		
		Command cmd = commands.get(name);
		if (cmd == null || !cmd.canUse(client)) {
			log.error("unknown command \"%s\"", name);
		} else {
			try {
				Parameters params = parser.parse(command, cmd.conditions());
				cmd.use(client, log, params);
			} catch (ParsingException e) {
				log.error(e.getLocalizedMessage());
			}
		}
	}
	
}
