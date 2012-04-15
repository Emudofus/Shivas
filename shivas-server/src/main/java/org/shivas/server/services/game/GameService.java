package org.shivas.server.services.game;

import org.shivas.protocol.client.types.GameServerType;
import org.shivas.server.services.Service;

public interface GameService extends Service {

	GameServerType informations();
	
}
