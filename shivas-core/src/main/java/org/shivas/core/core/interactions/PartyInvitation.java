package org.shivas.core.core.interactions;

import org.shivas.protocol.client.formatters.PartyGameMessageFormatter;
import org.shivas.core.core.parties.Party;
import org.shivas.core.services.game.GameClient;

public class PartyInvitation extends Invitation {

	public PartyInvitation(GameClient source, GameClient target) {
		super(source, target);
	}

	@Override
	public InteractionType getInteractionType() {
		return InteractionType.PARTY_INVITATION;
	}

	@Override
	public void begin() throws InteractionException {
		writeToAll(PartyGameMessageFormatter.invitationSuccessMessage(
                source.player().getName(),
                target.player().getName()
        ));
	}
	
	private static void process(Party party, GameClient client) {
		client.setParty(party);
		party.add(client.player());
		
		client.write(PartyGameMessageFormatter.createPartyMessage(party.getOwner().getName()));
		client.write(PartyGameMessageFormatter.leaderInformationMessage(party.getOwner().getId()));
		client.write(PartyGameMessageFormatter.addMembersMessage(party.toBasePartyMemberType()));
		
		party.subscribe(client.eventListener());
	}

	@Override
	public void accept() throws InteractionException {
		Party party = source.party();
		
		if (party == null) {
			party = new Party(source.player());

			process(party, source);
		}
		
		source.write(PartyGameMessageFormatter.declineInvitationMessage());

		process(party, target);
	}

	@Override
	public void decline() throws InteractionException {
        writeToAll(PartyGameMessageFormatter.declineInvitationMessage());
	}

}
