package org.shivas.server.core.plugins;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;

public class GroovyPluginLoader implements PluginLoader {
	
	public static final String EXTENSION = "groovy";
	public static final String LANGUAGE_NAME = "Groovy";
	
	private final GroovyShell shell;
	
	public GroovyPluginLoader(Object services) {
		shell = new GroovyShell(getClass().getClassLoader());
		shell.setVariable("shivaas", services);
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
