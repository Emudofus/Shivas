package org.shivas.server.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.shivas.server.database.RepositoryContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractService implements Service, IoHandler {
	
	private static final int BOTH_IDLE_TIME = 10;
	private static final int READ_BUFFER_SIZE = 512;
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final String ENCODING_DELIMITER = String.valueOf((char)0);
	private static final String DECODING_DELIMITER = "\n" + ENCODING_DELIMITER;
	
	protected final RepositoryContainer repositories;
	protected final IoAcceptor acceptor;
	
	private final Logger log;

	public AbstractService(RepositoryContainer repositories, int port, Logger log) {
		this.repositories = repositories;
		
		this.acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors());
		this.acceptor.getSessionConfig().setBothIdleTime(BOTH_IDLE_TIME);
		this.acceptor.getSessionConfig().setReadBufferSize(READ_BUFFER_SIZE);
		this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(
				CHARSET,
				ENCODING_DELIMITER,
				DECODING_DELIMITER
		)));
		this.acceptor.setHandler(this);
		this.acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
		
		this.log = log;
	}
	
	public AbstractService(RepositoryContainer repositories, int port) {
		this(repositories, port, LoggerFactory.getLogger(AbstractService.class));
	}

	public void start() {
		try {
			acceptor.bind();
			
			log.info("listen on {}", acceptor.getDefaultLocalAddress());
		} catch (IOException e) {
			log.error("Can't listen on {} because : {}", 
					acceptor.getDefaultLocalAddress(), 
					e.getMessage()
			);
		}
	}

	public void stop() {
		acceptor.unbind();
		acceptor.dispose();
		
		log.info("stopped");
	}

	public RepositoryContainer repositories() {
		return repositories;
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
		
		if (cause instanceof CriticalException) {
			session.close(false);
		}
	}

}
