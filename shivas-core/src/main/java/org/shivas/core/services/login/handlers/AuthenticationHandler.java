package org.shivas.core.services.login.handlers;

import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.CipherException;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;
import org.shivas.core.database.models.Account;
import org.shivas.core.services.AbstractBaseHandler;
import org.shivas.core.services.login.LoginClient;

public class AuthenticationHandler extends AbstractBaseHandler<LoginClient> {

	public AuthenticationHandler(LoginClient client) {
		super(client);
	}

	public void init() throws Exception {
	}

	public void onClosed() {
	}
	
	private boolean checkPassword(Account account, String input) throws CipherException {
		Cipher cipher = client.service().makeCipher(client.ticket(), account);
		
		return cipher.cipher(input).equalsIgnoreCase(account.getPassword());
	}
	
	private String getError(Account account, String password) throws CipherException {
		if (account == null) {
			return LoginMessageFormatter.accessDenied();
		} else if (account.isConnected()) {
			return LoginMessageFormatter.alreadyConnected();
		} else if (account.isBanned()) {
			return LoginMessageFormatter.banned();
		} else if (!checkPassword(account, password)) {
			return LoginMessageFormatter.accessDenied();
		} else {
			return null;
		}
	}

	public void handle(String message) throws Exception {
		if (message.equals("Af")) return; // ignore
		
		String[] args = message.split("\n");
		if (args.length != 2) {
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
		
		Account account = client.service().repositories().accounts().find(args[0]);
		
		String error = getError(account, args[1].substring(2));
		if (error != null) {
			client.write(error);
			client.kick();
		} else {
			client.write(LoginMessageFormatter.nicknameInformationMessage(account.getNickname()));
			client.write(LoginMessageFormatter.communityInformationMessage(account.getCommunity()));
			client.write(LoginMessageFormatter.serverInformationMessage(client.service().game().informations()));
			client.write(LoginMessageFormatter.identificationSuccessMessage(account.hasRights()));
			client.write(LoginMessageFormatter.accountQuestionInformationMessage(account.getSecretQuestion()));
			
			client.setAccount(account);
			client.newHandler(new ServerChoiceHandler(client));
		}
	}

}
