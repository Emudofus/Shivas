package org.shivas.server.core.logging;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.server.config.Config;

public class ConsoleLogger implements DofusLogger { // TODO console logger
	
	private final IoSession session;
	private final Config config;
	
	public ConsoleLogger(IoSession session, Config config) {
		this.session = session;
		this.config = config;
	}

	@Override
	public void log(String pattern, Object... args) {
		session.write(BasicGameMessageFormatter.consoleMessage(String.format(pattern, args)));
	}
	
	private void logColor(String level, int color, String pattern, Object... args) {
		session.write(BasicGameMessageFormatter.consoleMessage("(" + level + ")" + String.format(pattern, args), color));
	}

	@Override
	public void info(String pattern, Object... args) {
		logColor(config.infoName(), 0, pattern, args);
	}

	@Override
	public void error(String pattern, Object... args) {
		logColor(config.errorName(), 1, pattern, args);
	}

	@Override
	public void warn(String pattern, Object... args) {
		logColor(config.warnName(), 2, pattern, args);
	}

}
