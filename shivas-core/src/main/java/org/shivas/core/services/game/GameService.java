package org.shivas.core.services.game;

import org.shivas.protocol.client.types.GameServerType;
import org.shivas.core.core.channels.ChannelContainer;
import org.shivas.core.core.commands.CommandEngine;
import org.shivas.core.core.fights.FightFactory;
import org.shivas.core.core.services.NetworkStatisticsCenter;
import org.shivas.core.services.Service;
import org.shivas.core.services.login.LoginService;

public interface GameService extends Service<GameClient> {
	
	LoginService login();
	ChannelContainer channels();
	CommandEngine cmdEngine();
	NetworkStatisticsCenter statistics();
    FightFactory fightFactory();

	GameServerType informations();
	
}
