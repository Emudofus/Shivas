package org.shivas.server.core.plugins;

import java.util.Collection;

import org.shivas.server.core.commands.Command;
import org.shivas.server.services.ServiceListener;
import org.shivas.server.services.game.GameClient;
import org.shivas.server.services.login.LoginClient;

import com.google.common.collect.Lists;

public abstract class Plugin {
	
	public Collection<Startable> getServices() {
		return Lists.newArrayList();
	}
	
	public Collection<Command> getCommands() {
		return Lists.newArrayList();
	}
	
	public Collection<ServiceListener<GameClient>> getGameListeners() {
		return Lists.newArrayList();
	}
	
	public Collection<ServiceListener<LoginClient>> getLoginListeners() {
		return Lists.newArrayList();
	}
	
}
