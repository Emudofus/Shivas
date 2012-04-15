package org.shivas.server.services.login.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.CipherException;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;
import org.shivas.server.database.models.Account;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.login.LoginClient;

public class AuthenticationHandler extends AbstractBaseHandler<LoginClient> {
	
	private Cipher cipher;

	public AuthenticationHandler(LoginClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		cipher = client.service().makeCipher(client.ticket());
		
		return this;
	}
	
	private boolean checkPassword(String expected, String input) throws CipherException {
		return cipher.cipher(input).equals(expected);
	}
	
	private String getError(Account account, String password) throws CipherException {
		if (account == null) {
			return LoginMessageFormatter.accessDenied();
		} else if (account.isConnected()) {
			return LoginMessageFormatter.alreadyConnected();
		} else if (account.isBanned()) {
			return LoginMessageFormatter.banned();
		} else if (!checkPassword(account.getPassword(), password)) {
			return LoginMessageFormatter.accessDenied();
		} else {
			return null;
		}
	}

	public void handle(String message) throws Exception {
		String[] args = message.split("\n");
		if (args.length != 2) {
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
		
		Account account = client.service().repositories().accounts().findByName(args[0]);
		
		String error = getError(account, args[1].substring(2));
		if (error != null) {
			session.write(error);
			kick();
		} else {
			// TODO
		}
	}

	public void onClosed() {
	}

}
