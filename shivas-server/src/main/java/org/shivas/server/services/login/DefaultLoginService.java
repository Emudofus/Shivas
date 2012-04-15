package org.shivas.server.services.login;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Ciphers;
import org.shivas.common.crypto.Dofus1DecrypterCipher;
import org.shivas.server.config.Config;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.services.AbstractService;
import org.shivas.server.services.login.handlers.VersionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultLoginService extends AbstractService implements LoginService {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultLoginService.class);
	private static final String CLIENT_TOKEN = "shivas.login.hander";
	
	private final Config config;

	@Inject
	public DefaultLoginService(RepositoryContainer repositories, Config config) {
		super(repositories, config.loginPort(), log);
		
		this.config = config;
	}
	
	public Config config() {
		return config;
	}

	public Cipher makeCipher(String ticket) {
		return Ciphers.combine(
				new Dofus1DecrypterCipher(ticket), 
				repositories.accounts().passwordCipher()
		);
	}

	public void sessionCreated(IoSession session) throws Exception {
		session.setAttribute(CLIENT_TOKEN, new DefaultLoginClient(this));
	}

	public void sessionOpened(IoSession session) throws Exception {
		DefaultLoginClient client = (DefaultLoginClient) session.getAttribute(CLIENT_TOKEN);
		client.newHandler(new VersionHandler(client, session));
		
		log.debug("{} is connected", session.getRemoteAddress());
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
		
		String message = (String) o;
		DefaultLoginClient client = (DefaultLoginClient) session.getAttribute(CLIENT_TOKEN);
		
		log.debug(String.format("receive %d bytes from %s : %s", 
				message.length(),
				session.getRemoteAddress(),
				message
		));
		
		client.handler().handle(message);
	}

	public void messageSent(IoSession session, Object o) throws Exception {
		if (!(o instanceof String)) {
			throw new Exception("incoming message isn't a String");
		}
		
		String message = (String) o;
		
		log.debug(String.format("send %d bytes to %s : %s", 
				message.length(),
				session.getRemoteAddress(),
				message
		));
	}

}
