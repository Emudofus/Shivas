package org.shivas.login.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.crypto.CipherException;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.database.models.Account;
import org.shivas.login.services.LoginService;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;

import static org.shivas.login.services.SessionTokens.*;

public class AuthenticationHandler implements IoSessionHandler<String> {
	
	private final IoSession session;
	private final LoginService service;
	private final String ticket;

	public AuthenticationHandler(IoSession session, LoginService service) {
		this.session = session;
		this.service = service;
		this.ticket = ticket(this.session);
	}

	public IoSessionHandler<String> init() throws Exception {
		return this;
	}
	
	private boolean matchedPassword(String input, Account account) throws CipherException {
		return service.getDecrypter(ticket).cipher(input).equals(account.getPassword());
	}

	public void handle(String message) throws Exception {
		if (message.contains("Af")) return; // TODO queue
		
		String[] data = message.split("\n");
		
		if (data.length != 2) {
			throw new Exception("bad data received");
		}
		
		String name = data[0], password = data[1].substring(2);
		
		Account account = service.getRepositories().getAccounts().findByName(name);
		
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
					service.getRepositories().getServers().findAllToGameServerType(), 
					account.isSubscriber()
			));
			session.write(LoginMessageFormatter.identificationSuccessMessage(account.hasRights()));
			session.write(LoginMessageFormatter.accountQuestionInformationMessage(account.getSecretQuestion()));
			
			handler(session, new ServerChoiceHandler(session, service, account)).init();
		}
	}

	public void onClosed() {
	}

}
