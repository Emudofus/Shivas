package org.shivas.core.core.plugins;

import java.io.File;
import java.io.FileReader;

import org.jruby.embed.ScriptingContainer;
import org.jruby.javasupport.JavaEmbedUtils.EvalUnit;
import org.jruby.runtime.builtin.IRubyObject;
import org.shivas.core.Hook;

public class RubyPluginLoader implements PluginLoader {

	public static final String EXTENSION = "rb";
	public static final String LANGUAGE_NAME = "Ruby";
	
	private final ScriptingContainer scripting;
	
	public RubyPluginLoader(Hook hook) {
		scripting = new ScriptingContainer();
		scripting.put("Shivas", hook);
	}
	
	@Override
	public Plugin load(File file) throws Exception {
		if (!file.getPath().endsWith("." + EXTENSION)) {
			throw new Exception("RubyPluginLoader can load only .rb files");
		}
		
		EvalUnit unit = scripting.parse(new FileReader(file), file.getPath());
		
		IRubyObject obj = unit.run();
		
		return (Plugin) obj.toJava(Plugin.class);
	}

}
