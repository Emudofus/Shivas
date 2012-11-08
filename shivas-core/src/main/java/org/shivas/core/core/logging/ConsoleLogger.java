package org.shivas.core.core.logging;

import org.apache.mina.core.session.IoSession;
import org.shivas.core.config.ConfigProvider;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;

public class ConsoleLogger implements DofusLogger {
	
	private final IoSession session;
	private final ConfigProvider config;
	
	public ConsoleLogger(IoSession session, ConfigProvider config) {
		this.session = session;
		this.config = config;
	}

	@Override
	public void log(String pattern, Object... args) {
		session.write(BasicGameMessageFormatter.consoleMessage(String.format(pattern, args), 0));
	}
	
	private void logColor(String level, int color, String pattern, Object... args) {
		session.write(BasicGameMessageFormatter.consoleMessage("(" + level + ") " + String.format(pattern, args), color));
	}

	@Override
	public void info(String pattern, Object... args) {
		logColor(config.getString("loggers.info.name", DEFAULT_INFO_NAME), 2, pattern, args);
	}

	@Override
	public void error(String pattern, Object... args) {
		logColor(config.getString("loggers.error.name", DEFAULT_ERROR_NAME), 1, pattern, args);
	}

	@Override
	public void warn(String pattern, Object... args) {
        logColor(config.getString("loggers.error.warn", DEFAULT_WARN_NAME), 0, pattern, args);
	}

}
