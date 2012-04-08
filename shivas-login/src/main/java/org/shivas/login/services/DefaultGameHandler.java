package org.shivas.login.services;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.shivas.login.database.models.GameServer;
import org.shivas.protocol.server.Message;
import org.shivas.protocol.server.codec.MamboProtocolCodecFactory;
import org.shivas.protocol.server.enums.ServerStatus;
import org.shivas.protocol.server.messages.HelloConnectMessage;
import org.shivas.protocol.server.messages.ServerStatusUpdateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGameHandler implements GameHandler, IoHandler {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultGameHandler.class);
	
	private final GameServer server;
	private final GameService service;
	
	private final IoConnector connector;
	private IoSession session;
	
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
			server.setStatus(((ServerStatusUpdateMessage)message).getStatus());
			break;
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
