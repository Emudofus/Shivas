package org.shivas.login.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.StringUtils;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.services.LoginClient;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;

public class VersionHandler extends AbstractBaseHandler {

	public VersionHandler(LoginClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		client.ticket(StringUtils.random(32));
		
		session.write(LoginMessageFormatter.helloConnect(client.ticket()));
		
		return this;
	}

	public void handle(String message) throws Exception {
		if (!message.equals(client.service().getConfig().getClientVersion())) {
			session.write(LoginMessageFormatter.badClientVersion(client.service().getConfig().getClientVersion()));
			session.close(true);
		} else {
			client.newHandler(new AuthenticationHandler(client, session));
		}
	}

	public void onClosed() {
	}

}
