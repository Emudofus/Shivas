package org.shivas.server.core.plugins;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.Container;
import org.shivas.server.config.Config;
import org.shivas.server.core.actions.ShivasActionFactory;
import org.shivas.server.core.commands.Command;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Singleton
public class PluginsManager {
	
	private static final Logger log = LoggerFactory.getLogger(PluginsManager.class);

	@SuppressWarnings("unused")
	private class Shivas {
		public Config getConfig() {
			return config;
		}

		public Container getCtner() {
			return ctner;
		}

		public GameService getGservice() {
			return gservice;
		}

		public RepositoryContainer getRepos() {
			return repos;
		}
		
		public ShivasActionFactory getActions() {
			return actions;
		}
	}

	private final Map<String, PluginLoader> loaders = Maps.newHashMap();
	private final List<Plugin> plugins = Lists.newArrayList();
	
	private Config config;
	private Container ctner;
	private GameService gservice;
	private RepositoryContainer repos;
	private ShivasActionFactory actions;
	
	public PluginsManager() {
		Shivas Shivas = new Shivas();
		
		loaders.put(GroovyPluginLoader.EXTENSION, new GroovyPluginLoader(Shivas));
		loaders.put(RubyPluginLoader.EXTENSION, new RubyPluginLoader(Shivas));
	}

	@Inject
	public void init(Config config, Container ctner, GameService gservice, RepositoryContainer repos, ShivasActionFactory actions) {
		this.config = config;
		this.ctner = ctner;
		this.gservice = gservice;
		this.repos = repos;
		this.actions = actions;
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
			gservice.cmdEngine().add(command);
		}
	}
	
}
