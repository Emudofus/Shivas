package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.ContactAddErrorEnum;
import org.shivas.protocol.client.formatters.EnnemyGameMessageFormatter;
import org.shivas.server.core.contacts.AlreadyAddedException;
import org.shivas.server.core.contacts.EgocentricAddException;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Contact;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;
import org.shivas.server.utils.Filters;

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
			parseAddMessage(message.charAt(2) == '%' || message.charAt(2) == '*' ?
					message.substring(3) :
					message.substring(2)
			);
			break;
			
		case 'D':
			parseDeleteMessage(message.charAt(2) == '%' || message.charAt(2) == '*' ?
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
			client.write(EnnemyGameMessageFormatter.addEnnemyErrorMessage(ContactAddErrorEnum.NOT_FOUND));
			return;
		}

		try {
			Contact contact = client.account().getContacts().add(target, Contact.Type.ENNEMY);
			
			client.write(EnnemyGameMessageFormatter.addEnnemyMessage(contact.toBaseContactType()));
		} catch (EgocentricAddException e) {
			client.write(EnnemyGameMessageFormatter.addEnnemyErrorMessage(ContactAddErrorEnum.EGOCENTRIC));
		} catch (AlreadyAddedException e) {
			client.write(EnnemyGameMessageFormatter.addEnnemyErrorMessage(ContactAddErrorEnum.ALREADY_ADDED));
		}
	}

	private void parseDeleteMessage(String name) {
		Account target = findAccountOrPlayer(name);
		if (target != null && client.account().getContacts().delete(target)) {
			client.write(EnnemyGameMessageFormatter.deleteEnnemyMessage());
		} else {
			client.write(EnnemyGameMessageFormatter.deleteEnnemyErrorMessage());
		}
	}

	private void parseListMessage() {
		client.write(EnnemyGameMessageFormatter.
				ennemyListMessage(client.account().getContacts().toBaseContactType(Filters.ENNEMY_CONTACT_FILTER)));
	}

}
