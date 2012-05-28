package org.shivas.server.services.login.handlers;

import org.shivas.protocol.client.formatters.LoginMessageFormatter;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.login.LoginClient;

public class ServerChoiceHandler extends AbstractBaseHandler<LoginClient> {

	public ServerChoiceHandler(LoginClient client) {
		super(client);
	}

	public void init() throws Exception {
		client.account().setConnected(true);
		client.service().repositories().accounts().save(client.account());
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

	public void onClosed() {
		client.account().setConnected(false);
		client.service().repositories().accounts().save(client.account());
	}

	private void parseCharactersListMessage() {		
		client.write(LoginMessageFormatter.charactersListMessage(
				client.account().getRemainingSubscription().getMillis(), 
				client.service().game().informations().getId(),
				client.account().getPlayers().size()
		));
	}

	private void parseServerSelectionMessage(int serverId) throws Exception {
		if (serverId != client.service().game().informations().getId()) {
			throw new Exception(String.format("invalid incoming data [serverId=%d]", serverId));
		}
		
		client.service().putAccount(client.ticket(), client.account());
		
		client.write(LoginMessageFormatter.selectedHostInformationMessage(
				client.service().game().informations().getAddress(),
				client.service().game().informations().getConnexionPort(),
				client.ticket(),
				isLoopback()
		));
		
		client.kick();
	}

	private void parseQueueInformationsMessage() {
		//TODO login queue
	}

}
