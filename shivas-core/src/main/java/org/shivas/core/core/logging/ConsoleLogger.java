package org.shivas.core.core.logging;

import com.typesafe.config.Config;
import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;

public class ConsoleLogger implements DofusLogger {
	
	private final IoSession session;
	private final Config config;
	
	public ConsoleLogger(IoSession session, Config config) {
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
		logColor(config.getString("shivas.loggers.info.name"), 2, pattern, args);
	}

	@Override
	public void error(String pattern, Object... args) {
		logColor(config.getString("shivas.loggers.error.name"), 1, pattern, args);
	}

	@Override
	public void warn(String pattern, Object... args) {
        logColor(config.getString("shivas.loggers.error.warn"), 0, pattern, args);
	}

}
