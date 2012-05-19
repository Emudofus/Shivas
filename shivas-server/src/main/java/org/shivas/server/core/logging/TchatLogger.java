package org.shivas.server.core.logging;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.server.config.Config;

public class TchatLogger implements DofusLogger {
	
	private static final String LOG_LEVEL_PATTERN = "<font color=\"%s\">(%s) %s</font>";
	
	private final IoSession session;
	private final Config config;

	public TchatLogger(IoSession session, Config config) {
		this.session = session;
		this.config = config;
	}

	public void log(String pattern, Object... args) {
		session.write(ChannelGameMessageFormatter.informationMessage(String.format(pattern, args)));
	}
	
	private void logLevel(String color, String level, String message, Object... args) {
		log(LOG_LEVEL_PATTERN, color, level, String.format(message, args));
	}

	public void info(String pattern, Object... args) {
		logLevel(config.infoColor(), config.infoName(), pattern, args);
	}

	public void error(String pattern, Object... args) {
		logLevel(config.errorColor(), config.errorName(), pattern, args);
	}

	public void warn(String pattern, Object... args) {
		logLevel(config.warnColor(), config.warnName(), pattern, args);
	}

}
