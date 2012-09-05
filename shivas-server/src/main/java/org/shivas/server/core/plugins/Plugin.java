package org.shivas.server.core.plugins;

import java.util.Collection;

import org.shivas.server.core.commands.Command;

import com.google.common.collect.Lists;

public abstract class Plugin {

	public abstract String getAuthor();
	public abstract String getVersion();
	public abstract String getHelp();
	
	public Collection<Startable> getServices() {
		return Lists.newArrayList();
	}
	
	public Collection<Command> getCommands() {
		return Lists.newArrayList();
	}
	
}
