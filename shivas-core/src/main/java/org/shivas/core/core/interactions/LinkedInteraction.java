package org.shivas.core.core.interactions;

import org.shivas.core.services.game.GameClient;

public interface LinkedInteraction extends Interaction {
	GameClient getSource();
	GameClient getTarget();
}
