package org.atomium.cfg;

public interface Configuration {
	
	String driver();
	
	String connection();
	
	String user();
	String password();
	
	int flushDelay();
	
}
