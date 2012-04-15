package org.shivas.server.services.login.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.login.LoginClient;

public class ServerChoiceHandler extends AbstractBaseHandler<LoginClient> {

	public ServerChoiceHandler(LoginClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		client.account().setConnected(true);
		client.service().repositories().accounts().update(client.account());

		return this;
	}

	public void handle(String message) throws Exception {
		if (!message.startsWith("A")) {
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
		
		switch (message.charAt(1)) {
		case 'x':
			parseCharactersListMessage();
			break;
			
		case 'X':
			parseServerSelectionMessage(Integer.parseInt(message.substring(2)));
			break;
			
		case 'f':
			parseQueueInformationsMessage();
			break;
			
		default:
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
	}

	private void parseCharactersListMessage() {		
		session.write(LoginMessageFormatter.charactersListMessage(
				client.account().getRemainingSubscription().getMillis(), 
				client.service().game().informations().getId(),
				client.account().getPlayers().size()
		));
	}

	private void parseServerSelectionMessage(int serverId) throws Exception {
		if (serverId != client.service().game().informations().getId()) {
			throw new Exception(String.format("invalid incoming data [serverId=%d]", serverId));
		}
		
		session.write(LoginMessageFormatter.selectedHostInformationMessage(
				client.service().game().informations().getAddress(),
				client.service().game().informations().getConnexionPort(),
				client.ticket(),
				isLoopback()
		));
	}

	private void parseQueueInformationsMessage() {
		//TODO
	}

	public void onClosed() {
		client.account().setConnected(false);
		client.service().repositories().accounts().update(client.account());
	}

}
