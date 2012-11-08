package org.shivas.core.core.logging;

public interface DofusLogger {
    public static String DEFAULT_INFO_NAME = "INFO", DEFAULT_INFO_COLOR = "#009900",
                         DEFAULT_ERROR_NAME = "ERROR", DEFAULT_ERROR_COLOR = "#C10000",
                         DEFAULT_WARN_NAME = "WARN", DEFAULT_WARN_COLOR = "#FF8000";

	void log(String pattern, Object... args);
	void info(String pattern, Object... args);
	void error(String pattern, Object... args);
	void warn(String pattern, Object... args);
}
