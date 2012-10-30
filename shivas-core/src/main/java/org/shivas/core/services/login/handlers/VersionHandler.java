package org.shivas.core.services.login.handlers;

import org.shivas.common.StringUtils;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;
import org.shivas.core.services.AbstractBaseHandler;
import org.shivas.core.services.login.LoginClient;

public class VersionHandler extends AbstractBaseHandler<LoginClient> {

	public VersionHandler(LoginClient client) {
		super(client);
	}

	public void init() throws Exception {
		client.setTicket(StringUtils.random(32));
		
		client.write(LoginMessageFormatter.helloConnect(client.ticket()));
	}

	public void handle(String message) throws Exception {
		String requiredVersion = client.service().config().clientVersion();
		
		if (requiredVersion.equals(message)) {
			client.newHandler(new AuthenticationHandler(client));
		} else {
			client.write(LoginMessageFormatter.badClientVersion(requiredVersion));
			client.kick();
		}
	}

	public void onClosed() {
	}

}
