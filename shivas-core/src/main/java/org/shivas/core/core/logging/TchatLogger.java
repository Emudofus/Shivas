package org.shivas.core.core.logging;

import com.typesafe.config.Config;
import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;

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
		logLevel(
                config.getString("shivas.loggers.info.color"),
                config.getString("shivas.loggers.info.name"),
                pattern, args
        );
	}

	public void error(String pattern, Object... args) {
        logLevel(
                config.getString("shivas.loggers.error.color"),
                config.getString("shivas.loggers.error.name"),
                pattern, args
        );
	}

	public void warn(String pattern, Object... args) {
        logLevel(
                config.getString("shivas.loggers.warn.color"),
                config.getString("shivas.loggers.warn.name"),
                pattern, args
        );
	}

}
