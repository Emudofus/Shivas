package org.shivas.core.core.plugins;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.core.Hook;
import org.shivas.core.config.Config;
import org.shivas.core.core.commands.Command;
import org.shivas.core.services.ServiceListener;
import org.shivas.core.services.game.GameClient;
import org.shivas.core.services.game.GameService;
import org.shivas.core.services.login.LoginClient;
import org.shivas.core.services.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Singleton
public class PluginsManager {
	
	private static final Logger log = LoggerFactory.getLogger(PluginsManager.class);

	private final Map<String, PluginLoader> loaders = Maps.newHashMap();
	private final List<Plugin> plugins = Lists.newArrayList();
	
	private Config config;
	private LoginService login;
	private GameService game;

	@Inject
	public void init(Hook hook) {
		loaders.put(GroovyPluginLoader.EXTENSION, new GroovyPluginLoader(hook));
		loaders.put(RubyPluginLoader.EXTENSION, new RubyPluginLoader(hook));
		
		this.config = hook.getConfig();
		this.login = hook.getLogin();
		this.game = hook.getGame();
	}
	
	public void start() {		
		for (Plugin plugin : plugins) {
			for (Startable service : plugin.getServices()) {
				service.start();
			}
		}
	}
	
	public void stop() {
		for (Plugin plugin : plugins) {
			for (Startable service : plugin.getServices()) {
				service.stop();
			}
		}
	}
	
	public void load() {
		load(new File(config.pluginPath()));
		
		log.info("{} plugins loaded", plugins.size());
	}
	
	private void load(File directory) {
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				load(file);
			} else {
				String path      = file.getPath(),
					   extension = path.substring(path.lastIndexOf('.') + 1);
				
				PluginLoader loader = loaders.get(extension);
				if (loader == null) {
					log.error("unknown '{}' file ({})", extension, path);
					continue;
				}
				
				try {
					Plugin plugin = loader.load(file);
					load(plugin);
				} catch (Exception e) {
					log.info("can't load '{}' because : {}", path, e.getMessage());
				}
			}
		}
	}
	
	private void load(Plugin plugin) {
		plugins.add(plugin);
		
		for (Command command : plugin.getCommands()) {
			game.cmdEngine().add(command);
		}
		
		for (ServiceListener<GameClient> listener : plugin.getGameListeners()) {
			game.addListener(listener);
		}
		
		for (ServiceListener<LoginClient> listener : plugin.getLoginListeners()) {
			login.addListener(listener);
		}
	}
	
}
