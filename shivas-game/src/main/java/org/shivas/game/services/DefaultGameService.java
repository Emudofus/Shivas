package org.shivas.game.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.shivas.game.configuration.GameConfig;
import org.shivas.game.database.RepositoryContainer;
import org.shivas.game.services.handlers.AuthenticationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.shivas.game.services.SessionTokens.*;

@Singleton
public class DefaultGameService implements GameService, IoHandler {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultGameService.class);
	
	private static final int IDLE_TIME = 10;
	private static final int READ_BUFFER_SIZE = 2048;
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final String ENCODING_DELIMITER = String.valueOf((char)0);
	private static final String DECODING_DELIMITER = "\n" + ENCODING_DELIMITER;
	
	private final GameConfig config;
	private final RepositoryContainer repositories;
	private final LoginService loginService;
	
	private final IoAcceptor acceptor;

	@Inject
	public DefaultGameService(GameConfig config, RepositoryContainer repositories, LoginService loginService) {
		this.config = config;
		this.repositories = repositories;
		this.loginService = loginService;
		
		this.acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors());
		this.acceptor.getSessionConfig().setBothIdleTime(IDLE_TIME);
		this.acceptor.getSessionConfig().setReadBufferSize(READ_BUFFER_SIZE);
		this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(
				CHARSET, 
				new LineDelimiter(ENCODING_DELIMITER),
				new LineDelimiter(DECODING_DELIMITER)
		)));
		this.acceptor.setHandler(this);
	}

	public void start() {
		try {
			acceptor.bind(new InetSocketAddress(config.getPort()));
			
			log.info("listening on {}", config.getPort());
		} catch (IOException e) {
			log.info("can't listen on {} because : {}", config.getPort(), e.getMessage());
		}
	}

	public void stop() {
		acceptor.unbind();
		acceptor.dispose();
		
		log.info("stopped");
	}

	public GameConfig getConfig() {
		return config;
	}

	public RepositoryContainer getRepositories() {
		return repositories;
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void sessionCreated(IoSession session) throws Exception {
	}

	public void sessionOpened(IoSession session) throws Exception {
		log.debug("{} is connected", session.getRemoteAddress());
		
		handler(session, new AuthenticationHandler(session, this)).init();
	}

	public void sessionClosed(IoSession session) throws Exception {
		log.debug("{} is disconnected", session.getRemoteAddress());
		
		handler(session).onClosed();
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.error("({}) uncatched exception : {}", session.getRemoteAddress(), cause.getMessage());
	}

	public void messageReceived(IoSession session, Object o) throws Exception {
		if (!(o instanceof String)) {
			throw new Exception("incoming data aren't a String");
		}
		
		String message = (String)o;
		
		log.debug(String.format("receive %d from %s : %s",
				message.length(),
				session.getRemoteAddress(),
				message
		));
		
		handler(session).handle(message);
	}

	public void messageSent(IoSession session, Object o) throws Exception {
		if (!(o instanceof String)) {
			throw new Exception("outcoming data aren't a String");
		}
		
		String message = (String)o;
		
		log.debug(String.format("send %d to %s : %s",
				message.length(),
				session.getRemoteAddress(),
				message
		));
	}

}
