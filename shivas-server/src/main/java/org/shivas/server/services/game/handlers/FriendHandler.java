package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.FriendAddErrorEnum;
import org.shivas.protocol.client.formatters.FriendGameMessageFormatter;
import org.shivas.server.core.contacts.AlreadyAddedException;
import org.shivas.server.core.contacts.EgocentricAddException;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Contact;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class FriendHandler extends AbstractBaseHandler<GameClient> {

	public FriendHandler(GameClient client) {
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
	
	private Account findAccountOrPlayer(String name) {
		Account target = client.service().repositories().accounts().findByNickname(name);
		if (target == null) {
			Player player = client.service().repositories().players().find(name);
			if (player != null) {
				target = player.getOwner();
			}
		}
		return target;
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

}
