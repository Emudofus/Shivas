package org.shivas.data;

import org.shivas.data.loader.XmlLoader;

public class Loaders {
	private Loaders() {}
	
	public static final String XML = "xml";
	
	public static Loader byExtension(String extension, EntityFactory factory) {
		if (extension.equals(XML)) {
			return new XmlLoader(factory);
		}
		return null;
	}
}
