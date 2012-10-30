package org.shivas.core.core.plugins;

import java.util.Collection;

import org.shivas.core.core.commands.Command;
import org.shivas.core.services.ServiceListener;
import org.shivas.core.services.game.GameClient;
import org.shivas.core.services.login.LoginClient;

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
