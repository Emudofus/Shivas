package org.shivas.server.core.logging;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;

public class ConsoleLogger implements DofusLogger { // TODO console logger
	
	private final IoSession session;
	
	public ConsoleLogger(IoSession session) {
		this.session = session;
	}

	@Override
	public void log(String pattern, Object... args) {
		session.write(BasicGameMessageFormatter.consoleMessage(String.format(pattern, args)));
	}

	@Override
	public void info(String pattern, Object... args) {
	}

	@Override
	public void error(String pattern, Object... args) {
	}

	@Override
	public void warn(String pattern, Object... args) {
	}

}
