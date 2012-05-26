package org.shivas.server.services;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.server.core.logging.ConsoleLogger;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.core.logging.TchatLogger;

public abstract class AbstractBaseHandler<C extends Client<?>>
	implements BaseHandler
{
	
	protected final C client;
	protected final IoSession session;
	
	public AbstractBaseHandler(C client, IoSession session) {
		this.client = client;
		this.session = session;
	}
	
	public void kick() {
		session.close(false);
	}
	
	public void kick(String message) {
		session.write(GameMessageFormatter.kickMessage("System", message));
		kick();
	}
	
	public boolean isLoopback() {
		return session.getRemoteAddress().toString().contains("127.0.0.1"); // TODO better implementation
	}
	
	protected void assertTrue(boolean b, String message, Object... obj) throws Exception {
		if (!b) throw new Exception(String.format(message, obj));
	}
	
	protected String getClearAddress() {
        String address = session.getRemoteAddress().toString();
        return address.substring(1, address.indexOf(':'));
	}
	
	public DofusLogger tchat(){
		return new TchatLogger(session, client.service().config());
	}
	
	public DofusLogger console(){
		return new ConsoleLogger(session);
	}
	
}
