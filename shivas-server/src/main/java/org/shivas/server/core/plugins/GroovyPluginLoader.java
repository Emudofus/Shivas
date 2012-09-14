package org.shivas.server.core.plugins;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;

import org.shivas.server.Hook;

public class GroovyPluginLoader implements PluginLoader {
	
	public static final String EXTENSION = "groovy";
	public static final String LANGUAGE_NAME = "Groovy";
	
	private final GroovyShell shell;
	
	public GroovyPluginLoader(Hook hook) {
		shell = new GroovyShell(getClass().getClassLoader());
		shell.setVariable("hook", hook);
	}

	@Override
	public Plugin load(File file) throws Exception {
		if (!file.getPath().endsWith("." + EXTENSION)) {
			throw new Exception("GroovyPluginLoader can load only .groovy files");
		}
		
		Script script = shell.parse(file);
		script.run();
		
		return (Plugin) script.getBinding().getVariable("plugin");
	}

}
