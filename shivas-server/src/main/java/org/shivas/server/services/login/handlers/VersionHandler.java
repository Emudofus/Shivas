package org.shivas.server.services.login.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.StringUtils;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.login.LoginClient;

public class VersionHandler extends AbstractBaseHandler<LoginClient> {

	public VersionHandler(LoginClient client, IoSession session) {
		super(client, session);
	}

	public void init() throws Exception {
		client.setTicket(StringUtils.random(32));
		
		session.write(LoginMessageFormatter.helloConnect(client.ticket()));
	}

	public void handle(String message) throws Exception {
		String requiredVersion = client.service().config().clientVersion();
		
		if (requiredVersion.equals(message)) {
			client.newHandler(new AuthenticationHandler(client, session));
		} else {
			session.write(LoginMessageFormatter.badClientVersion(requiredVersion));
			kick();
		}
	}

	public void onClosed() {
	}

}
