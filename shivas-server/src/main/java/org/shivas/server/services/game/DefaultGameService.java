package org.shivas.server.services.game;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.enums.WorldStateEnum;
import org.shivas.protocol.client.types.GameServerType;
import org.shivas.server.config.Config;
import org.shivas.server.core.channels.ChannelContainer;
import org.shivas.server.core.commands.CommandEngine;
import org.shivas.server.core.services.NetworkStatisticsCenter;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.AbstractService;
import org.shivas.server.services.game.handlers.AuthenticationHandler;
import org.shivas.server.services.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultGameService extends AbstractService<GameClient> implements GameService {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultGameService.class);
	
	private static final String CLIENT_TOKEN = "shivas.game.client";
	
	private final Config config;
	private final LoginService login;
	private final GameServerType informations;
	private final ChannelContainer channels;
	private final CommandEngine cmdEngine;
	private final NetworkStatisticsCenter statistics;

	@Inject
	public DefaultGameService(RepositoryContainer repositories, Config config, LoginService login, ChannelContainer channels, CommandEngine cmdengine, NetworkStatisticsCenter statistics) {
		super(repositories, config.gamePort(), log);
		
		this.config = config;
		this.login = login;
		this.informations = new GameServerType(
				config.gameId(),
				config.gameAddress(),
				config.gamePort(),
				WorldStateEnum.ONLINE,
				0,
				true
		);
		this.channels = channels;
		this.cmdEngine = cmdengine;
		this.statistics = statistics;
	}

	public Config config() {
		return config;
	}

	public LoginService login() {
		return login;
	}

	public GameServerType informations() {
		return informations;
	}
	
	public ChannelContainer channels() {
		return channels;
	}
	
	public CommandEngine cmdEngine() {
		return cmdEngine;
	}

	public NetworkStatisticsCenter statistics() {
		return statistics;
	}

	public void sessionCreated(IoSession session) throws Exception {
		session.setAttribute(CLIENT_TOKEN, new DefaultGameClient(session, this));
	}

	public void sessionOpened(IoSession session) throws Exception {
		log.debug("{} is connected", session.getRemoteAddress());
		
		DefaultGameClient client = (DefaultGameClient) session.getAttribute(CLIENT_TOKEN);
		client.newHandler(new AuthenticationHandler(client));
		
		client.eventListener().add(new DefaultEventListener(client));
		
		connected(client);
	}

	public void sessionClosed(IoSession session) throws Exception {
		DefaultGameClient client = (DefaultGameClient) session.getAttribute(CLIENT_TOKEN);
		client.handler().onClosed();
		
		log.debug("{} is disconnected", session.getRemoteAddress());
		
		disconnected(client);
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	public void messageReceived(IoSession session, Object o) throws Exception {
		if (!(o instanceof String)) {
			throw new Exception("incoming message isn't a String");
		}
		
		DefaultGameClient client = (DefaultGameClient) session.getAttribute(CLIENT_TOKEN);
		String message = (String) o;
		
		log.debug(String.format("receive %d bytes from %s : %s",
				message.length(),
				session.getRemoteAddress(),
				message
		));
		
		if (message.equals("ping")) {
			session.write("pong");
		} else if (message.equals("qping")) {
			session.write("qpong");
		} else {
			NetworkStatisticsCenter.Chrono chrono = statistics.start(message);
			
			client.handler().handle(message);

			chrono.end();
			
			log.trace("it takes {} ms to handle \"{}\"", chrono.getDelta(), message);
		}
	}

	public void messageSent(IoSession session, Object o) throws Exception {
		if (!(o instanceof String)) {
			throw new Exception("outcoming message isn't a String");
		}
		
		String message = (String) o;
		
		log.debug(String.format("send %d bytes to %s : %s",
				message.length(),
				session.getRemoteAddress(),
				message
		));
	}

}
