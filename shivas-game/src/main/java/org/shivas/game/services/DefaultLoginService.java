package org.shivas.game.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.shivas.game.configuration.GameConfig;
import org.shivas.game.database.RepositoryContainer;
import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.codec.MamboProtocolCodecFactory;
import org.shivas.protocol.server.messages.AccountCharactersMessage;
import org.shivas.protocol.server.messages.AccountCharactersRequestMessage;
import org.shivas.protocol.server.messages.ClientConnectionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

@Singleton
public class DefaultLoginService implements LoginService, IoHandler {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultLoginService.class);
	
	private static final int BOTH_IDLE_TIME = 10;
	private static final int READ_BUFFER_SIZE = 256;

	private final GameConfig config;
	private final RepositoryContainer repositories;
	
	private final IoAcceptor acceptor;
	private IoSession session;
	
	private Map<String, Integer> accountByTicket = Maps.newConcurrentMap();
	
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
			
			log.info("waiting for login server's connection");
			
			// TODO login server's connection delay
		} catch (IOException e) {
			log.error("can't start because : {}", e.getMessage());
		}
	}

	public void stop() {
		acceptor.unbind();
		acceptor.dispose();
	}

	public Integer getAccount(String ticket) {
		return accountByTicket.remove(ticket);
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
		log.error("uncatched exception : {}", cause.getMessage());
	}

	public void messageReceived(IoSession session, Object obj) throws Exception {
		if (!(obj instanceof Message)) {
			throw new Exception("incoming data aren't a Message");
		}
		
		Message message = (Message)obj;
		
		log.debug("receive {}", message.getMessageType().getClass().getSimpleName());
		
		switch (message.getMessageType()) {
		case HELLO_CONNECT:
			this.session = session;
			log.info("login server's synchronization success");
			break;
		
		case ACCOUNT_CHARACTERS_REQUEST:
			parseAccountCharactersRequest((AccountCharactersRequestMessage)message);
			break;
			
		case CLIENT_CONNECTION:
			parseClientConnectionMessage((ClientConnectionMessage)message);
			break;
			
		case CLIENT_DECONNECTION:
			// TODO kick client
			break;
		}
	}

	private void parseAccountCharactersRequest(AccountCharactersRequestMessage message) {
		Integer count = repositories.getPlayers().countByOwner(message.getAccountId());
		
		if (count == null) count = 0;
		
		session.write(new AccountCharactersMessage(message.getAccountId(), count.byteValue()));
	}

	private void parseClientConnectionMessage(ClientConnectionMessage message) {
		accountByTicket.put(message.getSalt(), message.getAccountId());
	}

	public void messageSent(IoSession session, Object obj) throws Exception {
		if (!(obj instanceof Message)) {
			throw new Exception("outcoming data aren't a Message");
		}
		
		Message message = (Message)obj;
		
		log.debug("send {}", message.getMessageType().getClass().getSimpleName());
	}

}
