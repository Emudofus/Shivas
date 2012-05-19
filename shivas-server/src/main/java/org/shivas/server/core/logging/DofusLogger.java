package org.shivas.server.core.logging;

public interface DofusLogger {
	void log(String pattern, Object... args);
	void info(String pattern, Object... args);
	void error(String pattern, Object... args);
	void warn(String pattern, Object... args);
}
