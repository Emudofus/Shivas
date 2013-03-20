package org.shivas.core.services.game;

import com.typesafe.config.Config;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.shivas.core.core.channels.ChannelContainer;
import org.shivas.core.core.commands.CommandEngine;
import org.shivas.core.core.fights.FightFactory;
import org.shivas.core.core.services.NetworkStatisticsCenter;
import org.shivas.core.database.RepositoryContainer;
import org.shivas.core.services.AbstractService;
import org.shivas.core.services.game.handlers.AuthenticationHandler;
import org.shivas.core.services.login.LoginService;
import org.shivas.protocol.client.enums.WorldStateEnum;
import org.shivas.protocol.client.types.GameServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    private final FightFactory fightFactory;

	@Inject
	public DefaultGameService(RepositoryContainer repositories, Config config, LoginService login, ChannelContainer channels, CommandEngine cmdengine, NetworkStatisticsCenter statistics, FightFactory fightFactory) {
		super(repositories, config.getNumber("shivas.game.port").shortValue(), log);
		
		this.config = config;
		this.login = login;
        this.fightFactory = fightFactory;
        this.informations = new GameServerType(
				config.getInt("shivas.services.game.id"),
				config.getString("shivas.services.game.address"),
				config.getNumber("shivas.services.game.port").shortValue(),
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

    public FightFactory fightFactory() {
        return fightFactory;
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
