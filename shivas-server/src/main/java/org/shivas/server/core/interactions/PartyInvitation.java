package org.shivas.server.core.interactions;

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
	}

	@Override
	public void accept() throws ActionException {
	}

	@Override
	public void decline() throws ActionException {
	}

}
