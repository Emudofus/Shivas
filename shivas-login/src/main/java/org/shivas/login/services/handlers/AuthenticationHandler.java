package org.shivas.login.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.Account;
import org.shivas.common.crypto.CipherException;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.services.LoginClient;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;

public class AuthenticationHandler extends AbstractBaseHandler {

	public AuthenticationHandler(LoginClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		return this;
	}
	
	private boolean matchedPassword(String input, Account account) throws CipherException {
		return client.service()
				     .getDecrypter(client.ticket())
				     .cipher(input)
			     .equals(account.getPassword());
	}

	public void handle(String message) throws Exception {
		if (message.contains("Af")) return; // TODO queue
		
		String[] data = message.split("\n");
		
		if (data.length != 2) {
			throw new Exception("bad data received");
		}
		
		String name = data[0], password = data[1].substring(2);
		
		Account account = client.service().getRepositories().getAccounts().findByName(name);
		
		if (account == null) {
			session.write(LoginMessageFormatter.accessDenied());
		} else if (account.isBanned()) {
			session.write(LoginMessageFormatter.banned());
		} else if (account.isConnected()) {
			session.write(LoginMessageFormatter.alreadyConnected());
		} else if (!matchedPassword(password, account)) {
			session.write(LoginMessageFormatter.accessDenied());
		} else {
			session.write(LoginMessageFormatter.nicknameInformationMessage(account.getNickname()));
			session.write(LoginMessageFormatter.communityInformationMessage(account.getCommunity()));
			session.write(LoginMessageFormatter.serversInformationsMessage(
					client.service().getRepositories().getServers().findAllToGameServerType(), 
					account.isSubscriber()
			));
			session.write(LoginMessageFormatter.identificationSuccessMessage(account.hasRights()));
			session.write(LoginMessageFormatter.accountQuestionInformationMessage(account.getSecretQuestion()));
			
			client.account(account);
			client.newHandler(new ServerChoiceHandler(client, session));
		}
	}

	public void onClosed() {
	}

}
