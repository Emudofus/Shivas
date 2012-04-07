package org.shivas.login.services;

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
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.config.LoginConfig;
import org.shivas.login.database.RepositoryContainer;
import org.shivas.login.services.handlers.VersionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultLoginService implements LoginService, IoHandler {

	private static final Logger log = LoggerFactory.getLogger(DefaultLoginService.class);
	
	private static final int IDLE_TIME = 10;
	private static final int READ_BUFFER_SIZE = 2048;
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final String ENCODING_DELIMITER = String.valueOf((char)0);
	private static final String DECODING_DELIMITER = ENCODING_DELIMITER;
	
	private final LoginConfig config;
	private final RepositoryContainer repositories;
	private final IoAcceptor acceptor;
	
	@Inject
	public DefaultLoginService(LoginConfig config, RepositoryContainer repositories) {
		this.config = config;
		this.repositories = repositories;
		
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
			log.error("can't listen on {} because : {}", config.getPort(), e.getMessage());
		}
	}

	public void stop() {
		acceptor.unbind();
		acceptor.dispose();
		
		log.info("stopped");
	}
	
	public LoginConfig getConfig() {
		return config;
	}
	
	public RepositoryContainer getRepositories() {
		return repositories;
	}

	public void sessionCreated(IoSession session) throws Exception {
		session.setAttribute(SessionTokens.HANDLER, new VersionHandler(session, this));
	}

	public void sessionOpened(IoSession session) throws Exception {
		log.debug("({}) connected", session.getRemoteAddress());
	}

	public void sessionClosed(IoSession session) throws Exception {
		@SuppressWarnings("unchecked")
		IoSessionHandler<String> handler = (IoSessionHandler<String>)session.getAttribute(SessionTokens.HANDLER);
		
		handler.onClosed();
		
		log.debug("({}) disconnected", session.getRemoteAddress());
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.error("({}) uncatched exception : {}",
				session.getRemoteAddress(),
				cause.getMessage()
		);
	}

	public void messageReceived(IoSession session, Object obj) throws Exception {
		if (!(obj instanceof String)) {
			throw new Exception("incoming message is not a String");
		}
		
		String message = (String)obj;
		
		if (log.isDebugEnabled()) {
			log.debug(String.format("(%s) received %d bytes : %s",
					session.getRemoteAddress(),
					message.length(),
					message
			));
		}
		
		@SuppressWarnings("unchecked")
		IoSessionHandler<String> handler = (IoSessionHandler<String>)session.getAttribute(SessionTokens.HANDLER);
		
		handler.handle(message);
	}

	public void messageSent(IoSession session, Object obj) throws Exception {
		if (!(obj instanceof String)) {
			throw new Exception("outcoming message is not a String");
		}
		
		if (log.isDebugEnabled()) {
			String message = (String)obj;
			log.debug(String.format("(%s) sended %d bytes : %s",
					session.getRemoteAddress(),
					message.length(),
					message
			));
		}
	}

}
