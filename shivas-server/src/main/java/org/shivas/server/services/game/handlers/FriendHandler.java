package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.FriendAddErrorEnum;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.protocol.client.formatters.FriendGameMessageFormatter;
import org.shivas.server.core.contacts.AlreadyAddedException;
import org.shivas.server.core.contacts.ContactList;
import org.shivas.server.core.contacts.EgocentricAddException;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Contact;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class FriendHandler extends AbstractBaseHandler<GameClient> {

	public FriendHandler(GameClient client) {
		super(client);
	}

	@Override
	public void init() throws Exception {
		client.account().getContacts().notifyOwnerConnection();
		client.account().getContacts().subscribeToFriends(client.eventListener());
	}

	@Override
	public void onClosed() {
		client.account().getContacts().unscribeFromFriends(client.eventListener());
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
			
		case 'O':
			parseEnableNotificationMessage(message.charAt(2) == '+');
			break;
		}
	}

	private void parseAddMessage(String name) {
		Account target = findAccountOrPlayer(name);
		if (target == null) {
			client.write(FriendGameMessageFormatter.addFriendErrorMessage(FriendAddErrorEnum.NOT_FOUND));
			return;
		}

		try {
			Contact contact = client.account().getContacts().add(target, Contact.Type.FRIEND);
			
			client.write(FriendGameMessageFormatter.addFriendMessage(contact.toBaseFriendType()));
		} catch (EgocentricAddException e) {
			client.write(FriendGameMessageFormatter.addFriendErrorMessage(FriendAddErrorEnum.EGOCENTRIC));
		} catch (AlreadyAddedException e) {
			client.write(FriendGameMessageFormatter.addFriendErrorMessage(FriendAddErrorEnum.ALREADY_ADDED));
		}
	}

	private void parseDeleteMessage(String name) {
		Account target = findAccountOrPlayer(name);
		if (target != null && client.account().getContacts().delete(target)) {
			client.write(FriendGameMessageFormatter.deleteFriendMessage());
		} else {
			client.write(FriendGameMessageFormatter.deleteFriendErrorMessage());
		}
	}

	private void parseListMessage() {
		client.write(FriendGameMessageFormatter.friendListMessage(client.account().getContacts().toBaseFriendType()));
	}
	
	private void parseEnableNotificationMessage(boolean enable) {
		ContactList contacts = client.account().getContacts();
		contacts.setNotificationListener(enable);
		contacts.subscribeToFriends(client.eventListener());
		// does it need a database update ?
		
		client.write(BasicGameMessageFormatter.noOperationMessage());
	}

}
