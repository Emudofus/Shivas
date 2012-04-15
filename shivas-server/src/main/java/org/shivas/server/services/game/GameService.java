package org.shivas.server.services.game;

import org.shivas.protocol.client.types.GameServerType;
import org.shivas.server.config.Config;
import org.shivas.server.services.Service;
import org.shivas.server.services.login.LoginService;

public interface GameService extends Service {
	
	Config config();
	LoginService login();

	GameServerType informations();
	
}
