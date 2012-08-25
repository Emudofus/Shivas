package org.shivas.server.services.login;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.shivas.common.crypto.Cipher;
import org.shivas.common.crypto.Ciphers;
import org.shivas.common.crypto.Dofus1DecrypterCipher;
import org.shivas.server.config.Config;
import org.shivas.server.database.RepositoryContainer;
import org.shivas.server.database.models.Account;
import org.shivas.server.services.AbstractService;
import org.shivas.server.services.game.GameService;
import org.shivas.server.services.login.handlers.VersionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

@Singleton
public class DefaultLoginService extends AbstractService implements LoginService {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultLoginService.class);
	private static final String CLIENT_TOKEN = "shivas.login.handler";
	
	private final Config config;
	private final GameService game;
	
	private Map<String, Account> accountByTicket = Maps.newHashMap();

	@Inject
	public DefaultLoginService(RepositoryContainer repositories, Config config, GameService game) {
		super(repositories, config.loginPort(), log);
		
		this.config = config;
		this.game = game;
	}
	
	public Config config() {
		return config;
	}
	
	public GameService game() {
		return game;
	}

	public Cipher makeCipher(String ticket, Account account) {
		return Ciphers.combine(
				new Dofus1DecrypterCipher(ticket), 
				repositories.accounts().passwordCipher(account)
		);
	}

	public void putAccount(String ticket, Account account) {
		accountByTicket.put(ticket, account);
	}

	public Account getAccount(String ticket) {
		return accountByTicket.remove(ticket);
	}

	public void sessionCreated(IoSession session) throws Exception {
		session.setAttribute(CLIENT_TOKEN, new DefaultLoginClient(session, this));
	}

	public void sessionOpened(IoSession session) throws Exception {
		log.debug("{} is connected", session.getRemoteAddress());
		
		DefaultLoginClient client = (DefaultLoginClient) session.getAttribute(CLIENT_TOKEN);
		client.newHandler(new VersionHandler(client));
	}

	public void sessionClosed(IoSession session) throws Exception {
		DefaultLoginClient client = (DefaultLoginClient) session.getAttribute(CLIENT_TOKEN);
		client.handler().onClosed();
		
		log.debug("{} is disconnected", session.getRemoteAddress());
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
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
