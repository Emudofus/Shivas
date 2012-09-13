package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.formatters.PartyGameMessageFormatter;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.PartyInvitation;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
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
			String kickTargetId = message.substring(2);
			parseQuitMessage(
					kickTargetId.length() > 0 ?
							Integer.parseInt(kickTargetId) :
							null
			);
			break;
		}
	}

	private void parseAcceptInvitationMessage() throws InteractionException {
		client.interactions().remove(PartyInvitation.class).accept();
	}

	private void parseInvitationMessage(String targetName) throws Exception {
		Player player = client.service().repositories().players().find(targetName);
		if (player == null) {
			client.write(PartyGameMessageFormatter.targetNotFoundMessage(targetName));
			return;
		}
		if (player.getClient().hasParty()) {
			client.write(PartyGameMessageFormatter.targetAlreadyInPartyMessage(targetName));
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
		
		client.interactions().push(new PartyInvitation(client, player.getClient())).begin();	
	}

	private void parseDeclineInvitationMessage() throws InteractionException {
		client.interactions().remove(PartyInvitation.class).decline();
	}

	private void parseQuitMessage(Integer targetId) throws CriticalException {
		assertTrue(client.hasParty(), "you are not in a party");
		
		Player target;
		if (targetId != null) {
			assertTrue(client.party().getOwner() == client.player(), "you are not the leader");
			target = client.party().get(targetId);
		} else {
			target = client.player();
		}
		
		assertFalse(target == null, "unknown member %d", targetId);
		
		client.party().remove(target);
	}

}
