package org.shivas.server.core.plugins;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.shivas.data.Container;
import org.shivas.server.config.Config;
import org.shivas.server.core.actions.ShivasActionFactory;
import org.shivas.server.core.commands.Command;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.game.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

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

	private final ScriptEngineManager mgr = new ScriptEngineManager();
	private final List<Plugin> plugins = Lists.newArrayList();
	
	private Config config;
	private Container ctner;
	private GameService gservice;
	private RepositoryContainer repos;
	private ShivasActionFactory actions;

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
		mgr.put("Shivas", new Shivas());
		
		load(new File(config.pluginPath()));
		
		log.info("{} plugins loaded", plugins.size());
	}
	
	private String getExtension(File file) {
		if (file.getPath().endsWith(".jar")) return "jar";
		
		String[] extensions = config.pluginExtensions();
		for (String extension : extensions) {
			if (file.getPath().endsWith("." + extension)) {
				return extension;
			}
		}
		return null;
	}
	
	private Plugin getScriptPlugin(File file, String extension) {
		Plugin plugin = null;
		try {
			ScriptEngine engine = mgr.getEngineByExtension(extension);
			engine.eval(new FileReader(file));
			
			plugin = (Plugin) engine.get("plugin");
		} catch (Exception e) {
			log.error("can't eval {} because : {}", file.getPath(), e.getMessage());
		}
		return plugin;
	}
	
	private void load(File directory) {		
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				load(file);
			} else {
				String extension = getExtension(file);
				if (extension == null)
					continue;

				Plugin plugin = extension.equalsIgnoreCase("jar") ?
						null : // TODO jar file plugins
						getScriptPlugin(file, extension);
				
				if (plugin != null) {
					load(plugin);
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
