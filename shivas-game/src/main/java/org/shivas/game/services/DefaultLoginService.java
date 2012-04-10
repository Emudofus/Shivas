package org.shivas.game.services;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.inject.Singleton;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.shivas.game.configuration.GameConfig;
import org.shivas.game.database.RepositoryContainer;
import org.shivas.protocol.server.codec.MamboProtocolCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultLoginService implements LoginService, IoHandler {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultLoginService.class);
	
	private static final int BOTH_IDLE_TIME = 10;
	private static final int READ_BUFFER_SIZE = 256;

	private final GameConfig config;
	private final RepositoryContainer repositories;
	
	private final IoAcceptor acceptor;
	private IoSession session;
	
	@Singleton
	public DefaultLoginService(GameConfig config, RepositoryContainer repositories) {
		this.config = config;
		this.repositories = repositories;
		
		this.acceptor = new NioSocketAcceptor();
		this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MamboProtocolCodecFactory()));
		this.acceptor.getSessionConfig().setBothIdleTime(BOTH_IDLE_TIME);
		this.acceptor.getSessionConfig().setReadBufferSize(READ_BUFFER_SIZE);
		this.acceptor.setHandler(this);
	}

	public void start() {
		try {
			acceptor.bind(new InetSocketAddress(config.getSystemPort()));
			
			log.info("started");
		} catch (IOException e) {
			log.error("can't start because : {}", e.getMessage());
		}
	}

	public void stop() {
		acceptor.unbind();
		acceptor.dispose();
	}

	public GameConfig getConfig() {
		return config;
	}

	public void sessionCreated(IoSession session) throws Exception {
	}

	public void sessionOpened(IoSession session) throws Exception {
	}

	public void sessionClosed(IoSession session) throws Exception {
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
	}

	public void messageReceived(IoSession session, Object message) throws Exception {
	}

	public void messageSent(IoSession session, Object message) throws Exception {
	}

}
