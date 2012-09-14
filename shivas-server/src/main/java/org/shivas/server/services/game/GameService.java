package org.shivas.server.services.game;

import org.shivas.protocol.client.types.GameServerType;
import org.shivas.server.core.channels.ChannelContainer;
import org.shivas.server.core.commands.CommandEngine;
import org.shivas.server.core.services.NetworkStatisticsCenter;
import org.shivas.server.services.Service;
import org.shivas.server.services.login.LoginService;

public interface GameService extends Service<GameClient> {
	
	LoginService login();
	ChannelContainer channels();
	CommandEngine cmdEngine();
	NetworkStatisticsCenter statistics();

	GameServerType informations();
	
}
