package org.shivas.data;

public class Containers {
	private Containers() {}
	
	private static Container INSTANCE;
	
	public static Container instance() {
		return INSTANCE;
	}
	
	public static void setInstance(Container ctner) {
		INSTANCE = ctner;
	}
}
