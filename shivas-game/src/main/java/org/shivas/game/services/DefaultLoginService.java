package org.shivas.game.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.shivas.common.Account;
import org.shivas.game.configuration.GameConfig;
import org.shivas.game.database.RepositoryContainer;
import org.shivas.protocol.client.enums.WorldStateEnum;
import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.MessageType;
import org.shivas.protocol.server.codec.MamboProtocolCodecFactory;
import org.shivas.protocol.server.messages.AccountCharactersMessage;
import org.shivas.protocol.server.messages.AccountCharactersRequestMessage;
import org.shivas.protocol.server.messages.ClientConnectionMessage;
import org.shivas.protocol.server.messages.ClientDeconnectionMessage;
import org.shivas.protocol.server.messages.ServerStatusUpdateMessage;
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
	private final GameService gameService;
	
	private final IoAcceptor acceptor;
	private IoSession session;
	
	private Map<String, Account> accountByTicket = Maps.newConcurrentMap();
	
	@Inject
	public DefaultLoginService(GameConfig config, RepositoryContainer repositories, GameService gameService) {
		this.config = config;
		this.repositories = repositories;
		this.gameService = gameService;
		
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

	public Account getAccount(String ticket) {
		return accountByTicket.remove(ticket);
	}

	public void deconnection(Account account) {
		session.write(new ClientDeconnectionMessage(account.getId()));
	}
	
	public void updateStatus(WorldStateEnum status) {
		session.write(new ServerStatusUpdateMessage(status));
	}

	public void sessionCreated(IoSession s) throws Exception {
		if (this.session != null) {
			throw new Exception("already synchronized");
		}
	}

	public void sessionOpened(IoSession s) throws Exception {
	}

	public void sessionClosed(IoSession s) throws Exception {
		this.session = null;
	}

	public void sessionIdle(IoSession s, IdleStatus status) throws Exception {
	}

	public void exceptionCaught(IoSession s, Throwable cause) throws Exception {
		log.error(String.format("uncatched %s %s : %s", 
				cause.getClass().getSimpleName(),
				cause.getStackTrace()[0],
				cause.getMessage()
		));
	}

	public void messageReceived(IoSession session, Object obj) throws Exception {
		if (!(obj instanceof Message)) {
			throw new Exception("incoming data aren't a Message");
		}
		
		Message message = (Message)obj;
		
		log.debug("receive {}", message.getMessageType());
		
		if (this.session == null) {
			if (message.getMessageType() != MessageType.HELLO_CONNECT) {
				throw new Exception("not synchronized");
			}
			this.session = session;
			updateStatus(gameService.getStatus());
		} else {
			switch (message.getMessageType()) {
			case HELLO_CONNECT:
				throw new Exception("already synchronized");
			
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
	}

	private void parseAccountCharactersRequest(AccountCharactersRequestMessage message) {
		Long count = repositories.getPlayers().countByOwner(message.getAccountId());
		
		if (count == null) count = (long) 0;
		
		session.write(new AccountCharactersMessage(message.getAccountId(), count.byteValue()));
	}

	private void parseClientConnectionMessage(ClientConnectionMessage message) {
		accountByTicket.put(message.getSalt(), message.getAccount());
	}

	public void messageSent(IoSession session, Object obj) throws Exception {
		if (!(obj instanceof Message)) {
			throw new Exception("outcoming data aren't a Message");
		}
		
		Message message = (Message)obj;
		
		log.debug("send {}", message.getMessageType());
	}

}
