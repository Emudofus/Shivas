package org.shivas.login.services.handlers;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.shivas.common.Account;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.database.models.GameServer;
import org.shivas.login.services.LoginService;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;
import org.shivas.protocol.client.types.BaseCharactersServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.shivas.login.services.SessionTokens.*;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

public class ServerChoiceHandler implements IoSessionHandler<String> {
	
	private static final Logger log = LoggerFactory.getLogger(ServerChoiceHandler.class);
	
	private IoSession session;
	private LoginService service;
	private Account account;

	public ServerChoiceHandler(IoSession session, LoginService service, Account account) {
		this.session = session;
		this.service = service;
		this.account = account;
	}

	public IoSessionHandler<String> init() throws Exception {
		account.setConnected(true);
		service.getRepositories().getAccounts().update(account);
		
		return this;
	}

	public void handle(String message) throws Exception {
		if (message.startsWith("A")) {
			throw new Exception("Invalid data received [" + message + "]");
		}
		
		switch (message.charAt(1)) {
		case 'x':
			parseCharactersListMessage();
			break;
			
		case 'X':
			parseServerChoiceMessage(Integer.parseInt(message.substring(2)));
			break;
			
		case 'f':
			break;
		}
	}

	private void parseCharactersListMessage() {
		Futures.addCallback(service.getGameService().getNbCharactersByAccount(account), new FutureCallback<List<BaseCharactersServerType>>() {
			public void onSuccess(List<BaseCharactersServerType> result) {
				Duration subscription = new Duration(DateTime.now(), account.getSubscriptionEnd());
				
				session.write(LoginMessageFormatter.charactersListMessage(
						subscription.getStandardSeconds(), 
						result
				));
			}

			public void onFailure(Throwable t) {
				log.error("can't get characters list because : {}", t.getMessage());
				
				session.close(true);
			}
		});
	}
	
	private void parseServerChoiceMessage(int serverId) throws Exception {
		GameServer selected = service.getRepositories().getServers().findById(serverId);
		
		if (selected == null) {
			session.write(LoginMessageFormatter.serverSelectionErrorMessage());
		} else if (!selected.isAvailable()) {
			session.write(LoginMessageFormatter.serverSelectionErrorMessage());
		} else if (selected.isRestricted() && !account.isSubscriber()) {
			session.write(LoginMessageFormatter.serverSelectionErrorMessage());
		} else {
			String ticket = ticket(session);
			
			selected.getHandler().connection(account, ticket);
			
			session.write(LoginMessageFormatter.selectedHostInformationMessage(
					selected.getAddress(), 
					selected.getPort(), 
					ticket, 
					session.getRemoteAddress().toString().contains("127.0.0.1") // TODO
			));
		}
	}

	public void onClosed() {
		account.setConnected(false);
		service.getRepositories().getAccounts().update(account);
	}

}
