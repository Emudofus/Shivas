package org.shivas.server.core.interactions;

import org.shivas.server.services.game.GameClient;

public interface LinkedInteraction extends Interaction {
	GameClient getSource();
	GameClient getTarget();
}
