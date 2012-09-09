package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.formatters.PartyGameMessageFormatter;
import org.shivas.server.core.interactions.ActionException;
import org.shivas.server.core.interactions.PartyInvitation;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class PartyHandler extends AbstractBaseHandler<GameClient> {

	public PartyHandler(GameClient client) {
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
			parseAcceptInvitationMessage();
			break;
			
		case 'I':
			parseInvitationMessage(message.substring(2));
			break;
			
		case 'R':
			parseDeclineInvitationMessage();
			break;
			
		case 'V':
			String kickTargetName = message.substring(2);
			parseQuitMessage(
					kickTargetName.length() > 0 ?
							kickTargetName :
							null
			);
			break;
		}
	}

	private void parseAcceptInvitationMessage() throws ActionException {
		PartyInvitation invitation = client.actions().remove();
		invitation.accept();
	}

	private void parseInvitationMessage(String targetName) throws Exception {
		Player player = client.service().repositories().players().find(targetName);
		if (player == null) {
			client.write(PartyGameMessageFormatter.targetNotFoundMessage(targetName));
			return;
		}
		
		if (client.party() != null) {
			if (client.party().isFull()) {
				client.write(PartyGameMessageFormatter.partyFullMessage(targetName));
				return;
			}
			if (client.party().contains(player)) {
				client.write(PartyGameMessageFormatter.targetAlreadyInPartyMessage(targetName));
				return;
			}
		}
		
		GameClient target = player.getClient();
		if (target == null) {
			client.write(PartyGameMessageFormatter.targetNotFoundMessage(targetName));
			return;
		}
		
		client.actions().push(new PartyInvitation(client, player.getClient())).begin();	
	}

	private void parseDeclineInvitationMessage() throws ActionException {
		PartyInvitation invitation = client.actions().remove();
		invitation.decline();
	}

	private void parseQuitMessage(String targetName) {
		// TODO Auto-generated method stub
		
	}

}
