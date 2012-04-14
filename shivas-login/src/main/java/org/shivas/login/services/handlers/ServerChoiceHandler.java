package org.shivas.login.services.handlers;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.database.models.GameServer;
import org.shivas.login.services.LoginClient;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;
import org.shivas.protocol.client.types.BaseCharactersServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

public class ServerChoiceHandler extends AbstractBaseHandler {

	private static final Logger log = LoggerFactory.getLogger(ServerChoiceHandler.class);
	
	public ServerChoiceHandler(LoginClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		client.account().setConnected(true);
		client.service().getRepositories().getAccounts().update(client.account());
		
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
		Futures.addCallback(client.service().getGameService().getNbCharactersByAccount(client.account()), new FutureCallback<List<BaseCharactersServerType>>() {
			public void onSuccess(List<BaseCharactersServerType> result) {
				Duration subscription = new Duration(
						DateTime.now(), 
						client.account().getSubscriptionEnd()
				);
				
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
		GameServer selected = client.service().getRepositories().getServers().findById(serverId);
		
		if (selected == null) {
			session.write(LoginMessageFormatter.serverSelectionErrorMessage());
		} else if (!selected.isAvailable()) {
			session.write(LoginMessageFormatter.serverSelectionErrorMessage());
		} else if (selected.isRestricted() && !client.account().isSubscriber()) {
			session.write(LoginMessageFormatter.serverSelectionErrorMessage());
		} else {			
			selected.getGameHandler().connection(client.account(), client.ticket());
			
			session.write(LoginMessageFormatter.selectedHostInformationMessage(
					selected.getAddress(), 
					selected.getPort(), 
					client.ticket(), 
					session.getRemoteAddress().toString().contains("127.0.0.1") // TODO
			));
			
			session.close(false);
		}
	}

	public void onClosed() {
		client.account().setConnected(false);
		client.service().getRepositories().getAccounts().update(client.account());
	}

}
