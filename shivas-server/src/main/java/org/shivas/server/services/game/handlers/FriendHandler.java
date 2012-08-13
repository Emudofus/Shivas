package org.shivas.server.services.game.handlers;

import org.atomium.LazyReference;
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
			boolean account = message.charAt(2) == '%';
			parseAddMessage(
					account ? message.substring(3) : message.substring(2),
					account
			);
			break;
			
		case 'L':
			parseListMessage();
			break;
		}
	}

	private void parseAddMessage(String raw, boolean accountRequest) {
		Account target = null;
		if (accountRequest) {
			target = client.service().repositories().accounts().findByNickname(raw);
		} else {
			Player player = client.service().repositories().players().find(raw);
			if (player != null) {
				target = player.getOwner();
			} else {
				client.write(FriendGameMessageFormatter.addFriendErrorMessage(FriendAddErrorEnum.NOT_FOUND));
				return;
			}
		}

		try {
			Contact contact = client.account().getContacts().add(LazyReference.create(target), Contact.Type.FRIEND);
			
			client.write(FriendGameMessageFormatter.addFriendMessage(contact.toBaseFriendType()));
		} catch (EgocentricAddException e) {
			client.write(FriendGameMessageFormatter.addFriendErrorMessage(FriendAddErrorEnum.EGOCENTRIC));
		} catch (AlreadyAddedException e) {
			client.write(FriendGameMessageFormatter.addFriendErrorMessage(FriendAddErrorEnum.ALREADY_ADDED));
		}
	}

	private void parseListMessage() {
	}

}
