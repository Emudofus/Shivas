package org.shivas.server.services.game.handlers;

import org.shivas.server.core.contacts.AlreadyAddedException;
import org.shivas.server.core.contacts.EgocentricAddException;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Contact;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class EnnemyHandler extends AbstractBaseHandler<GameClient> {

	public EnnemyHandler(GameClient client) {
		super(client);
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public void onClosed() {
	}

	@Override
	public void handle(String message) throws Exception {
		switch (message.charAt(1)) {
		case 'A':
			parseAddMessage(message.charAt(2) == '%' ?
					message.substring(3) :
					message.substring(2)
			);
			break;
		
		case 'L':
			parseListMessage();
			break;
		}
	}

	private void parseAddMessage(String name) {
		Account target = findAccountOrPlayer(name);
		if (target == null) {
			return;
		}
		
		try {
			client.account().getContacts().add(target, Contact.Type.ENNEMY);
		} catch (EgocentricAddException e) {
		} catch (AlreadyAddedException e) {
		}
	}

	private void parseListMessage() {
		// TODO enemies
	}

}
