package org.shivas.server.services.game;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.enums.WorldStateEnum;
import org.shivas.protocol.client.types.GameServerType;
import org.shivas.server.config.Config;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.AbstractService;
import org.shivas.server.services.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGameService extends AbstractService implements GameService {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultGameService.class);
	
	private static final String CLIENT_TOKEN = "shivas.game.client";
	
	private final Config config;
	private final LoginService login;
	private final GameServerType informations;

	public DefaultGameService(RepositoryContainer repositories, Config config, LoginService login) {
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

	public void sessionCreated(IoSession session) throws Exception {
		session.setAttribute(CLIENT_TOKEN, new DefaultGameClient(this));
	}

	public void sessionOpened(IoSession session) throws Exception {
		log.debug("{} is connected", session.getRemoteAddress());
		
		DefaultGameClient client = (DefaultGameClient) session.getAttribute(CLIENT_TOKEN);
		// TODO set new handler
	}

	public void sessionClosed(IoSession session) throws Exception {
		log.debug("{} is disconnected", session.getRemoteAddress());
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		String message;
		if (session != null) {
			message = String.format("(%s) uncatched %s %s : %s",
					session.getRemoteAddress(),
					cause.getClass().getSimpleName(),
					cause.getStackTrace()[0],
					cause.getMessage()
			);
		} else {
			message = String.format("uncatched %s %s : %s",
					cause.getClass().getSimpleName(),
					cause.getStackTrace()[0],
					cause.getMessage()
			);
		}
		
		log.error(message);
	}

	public void messageReceived(IoSession session, Object o) throws Exception {
		if (!(o instanceof String)) {
			throw new Exception("incoming message isn't a String");
		}
		
		DefaultGameClient client = (DefaultGameClient) session.getAttribute(CLIENT_TOKEN);
		String message = (String) o;
		
		log.debug(String.format("receive %d bytes from %s : %",
				message.length(),
				session.getRemoteAddress(),
				message
		));
		
		client.handler().handle(message);
	}

	public void messageSent(IoSession session, Object o) throws Exception {
		if (!(o instanceof String)) {
			throw new Exception("outcoming message isn't a String");
		}
		
		String message = (String) o;
		
		log.debug(String.format("send %d bytes to %s : %",
				message.length(),
				session.getRemoteAddress(),
				message
		));
	}

}
