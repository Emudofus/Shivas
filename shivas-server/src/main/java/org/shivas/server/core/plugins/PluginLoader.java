package org.shivas.server.core.plugins;

import java.io.File;

public interface PluginLoader {

	Plugin load(File file) throws Exception;
	
}
