package org.shivas.login.services;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.shivas.login.database.models.Account;
import org.shivas.login.database.models.GameServer;
import org.shivas.protocol.client.enums.WorldStateEnum;
import org.shivas.protocol.client.types.BaseCharactersServerType;
import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.codec.MamboProtocolCodecFactory;
import org.shivas.protocol.server.messages.AccountCharactersMessage;
import org.shivas.protocol.server.messages.AccountCharactersRequestMessage;
import org.shivas.protocol.server.messages.ClientDeconnectionMessage;
import org.shivas.protocol.server.messages.HelloConnectMessage;
import org.shivas.protocol.server.messages.ServerStatusUpdateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

public class DefaultGameHandler implements GameHandler, IoHandler {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultGameHandler.class);
	
	private final GameServer server;
	private final GameService service;
	
	private final IoConnector connector;
	private IoSession session;
	private Map<Integer, SettableFuture<BaseCharactersServerType>> nbCharactersByAccountId = Maps.newHashMap();
	
	private WorldStateEnum status;
	
	public DefaultGameHandler(GameServer server, GameService service) {
		this.server = server;
		this.service = service;
		
		this.connector = new NioSocketConnector();
		this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MamboProtocolCodecFactory()));
		this.connector.getSessionConfig().setBothIdleTime(10);
		this.connector.getSessionConfig().setReadBufferSize(128);
		this.connector.setHandler(this);
	}

	public void start() {
		connector.connect(new InetSocketAddress(server.getSystemAddress(), server.getSystemPort()));
	}

	public void stop() {
		session.close(true);
		connector.dispose();
	}

	public WorldStateEnum getStatus() {
		return status;
	}

	public boolean isAvailable() {
		return status == WorldStateEnum.ONLINE;
	}

	public ListenableFuture<BaseCharactersServerType> getNbCharacters(Account account) {
		SettableFuture<BaseCharactersServerType> future = SettableFuture.create();
		nbCharactersByAccountId.put(account.getId(), future);
		session.write(new AccountCharactersRequestMessage(account.getId()));
		return future;
	}

	public void sessionCreated(IoSession session) throws Exception {
	}

	public void sessionOpened(IoSession session) throws Exception {
		this.session = session;
		
		log.info("{} is online", server.getName());
		
		session.write(new HelloConnectMessage());
	}

	public void sessionClosed(IoSession session) throws Exception {
		this.session = null;
		
		log.error("{} is offline", server.getName());
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.error("({}) uncatched exception : {}", server.getName(), cause.getMessage());
	}

	public void messageReceived(IoSession session, Object obj) throws Exception {
		if (!(obj instanceof Message)) {
			throw new Exception("");
		}
		
		Message message = (Message)obj;
		
		log.debug("received from {} : {}", message.getMessageType().getClass().getSimpleName(), server.getName());
		
		switch (message.getMessageType()) {
		case SERVER_STATUS_UPDATE:
			status = ((ServerStatusUpdateMessage)message).getStatus();
			break;
			
		case ACCOUNT_CHARACTERS:
			parseAccountCharactersMessage((AccountCharactersMessage)message);
			break;
			
		case CLIENT_DECONNECTION:
			parseClientDeconnectionMessage((ClientDeconnectionMessage)message);
			break;
		}
	}
	

	private void parseAccountCharactersMessage(AccountCharactersMessage msg) {
		SettableFuture<BaseCharactersServerType> future = nbCharactersByAccountId.get(msg.getAccountId());
		if (future != null) {
			future.set(new BaseCharactersServerType(server.getId(), msg.getCharacters()));
		} else {
			log.warn("({}) {} doesn't requested its nb of characters", server.getName(), msg.getAccountId());
		}
	}

	private void parseClientDeconnectionMessage(ClientDeconnectionMessage msg) {
		Account account = service.getRepositories().getAccounts().findById(msg.getAccountId());
		
		if (account != null) {
			account.setConnected(false);
			service.getRepositories().getAccounts().update(account);
		} else {
			log.warn("({}) account #{} doesn't exist", msg.getAccountId());
		}
	}

	public void messageSent(IoSession session, Object obj) throws Exception {
		if (!(obj instanceof Message)) {
			throw new Exception("");
		}
		
		Message message = (Message)obj;
		
		log.debug("sended to {} : {}", message.getMessageType().getClass().getSimpleName(), server.getName());
	}

}
