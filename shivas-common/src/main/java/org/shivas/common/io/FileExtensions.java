package org.shivas.common.io;

import java.io.File;

public class FileExtensions {
	private FileExtensions() {}
	
	public static boolean match(File file, String extension) {
		return getExtension(file).equals(extension);
	}

	public static String getExtension(File file) {
		String name = file.getName();
		return name.substring(name.indexOf('.') + 1);
	}
}
