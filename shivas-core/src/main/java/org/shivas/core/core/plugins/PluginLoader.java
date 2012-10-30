package org.shivas.core.core.plugins;

import java.io.File;

public interface PluginLoader {

	Plugin load(File file) throws Exception;
	
}
