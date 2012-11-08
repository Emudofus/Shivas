package org.shivas.core.core.logging;

import org.apache.mina.core.session.IoSession;
import org.shivas.core.config.ConfigProvider;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;

public class TchatLogger implements DofusLogger {
	
	private static final String LOG_LEVEL_PATTERN = "<font color=\"%s\">(%s) %s</font>";
	
	private final IoSession session;
	private final ConfigProvider config;

	public TchatLogger(IoSession session, ConfigProvider config) {
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
		logLevel(
                config.getString("loggers.info.color", DEFAULT_INFO_COLOR),
                config.getString("loggers.info.name", DEFAULT_INFO_NAME),
                pattern, args
        );
	}

	public void error(String pattern, Object... args) {
        logLevel(
                config.getString("loggers.error.color", DEFAULT_ERROR_COLOR),
                config.getString("loggers.error.name", DEFAULT_ERROR_NAME),
                pattern, args
        );
	}

	public void warn(String pattern, Object... args) {
        logLevel(
                config.getString("loggers.warn.color", DEFAULT_WARN_COLOR),
                config.getString("loggers.warn.name", DEFAULT_WARN_NAME),
                pattern, args
        );
	}

}
