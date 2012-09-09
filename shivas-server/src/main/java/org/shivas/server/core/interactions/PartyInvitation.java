package org.shivas.server.core.interactions;

import org.shivas.protocol.client.formatters.PartyGameMessageFormatter;
import org.shivas.server.core.parties.Party;
import org.shivas.server.services.game.GameClient;

public class PartyInvitation extends Invitation {

	public PartyInvitation(GameClient source, GameClient target) {
		super(source, target);
	}

	@Override
	public ActionType actionType() {
		return ActionType.PARTY_INVITATION;
	}

	@Override
	public void begin() throws ActionException {
		String message = PartyGameMessageFormatter.invitationSuccessMessage(
				source.player().getName(),
				target.player().getName()
		);
		
		source.write(message);
		target.write(message);
	}
	
	private static void process(Party party, GameClient client, boolean leader) {
		client.setParty(party);
		if (!leader) {
			party.add(client.player());
		}
		
		party.subscribe(client.eventListener());
		
		client.write(PartyGameMessageFormatter.createPartyMessage(party.getOwner().getName()));
		client.write(PartyGameMessageFormatter.leaderInformationMessage(party.getOwner().getId()));
		if (leader) {
			client.write(PartyGameMessageFormatter.addMemberMessage(client.player().toBasePartyMemberType()));
			client.write(PartyGameMessageFormatter.declineInvitationMessage());
		} else {
			client.write(PartyGameMessageFormatter.addMembersMessage(party.toBasePartyMemberType()));
		}
	}

	@Override
	public void accept() throws ActionException {
		Party party = source.party();
		
		if (party == null) {
			party = new Party(source.player());

			process(party, source, true);
		}

		process(party, target, false);
	}

	@Override
	public void decline() throws ActionException {
		source.write(PartyGameMessageFormatter.declineInvitationMessage());
		target.write(PartyGameMessageFormatter.declineInvitationMessage());
	}

}
