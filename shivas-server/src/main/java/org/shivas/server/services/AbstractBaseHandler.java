package org.shivas.server.services;

import org.shivas.server.core.logging.ConsoleLogger;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.core.logging.TchatLogger;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;

public abstract class AbstractBaseHandler<C extends Client<?>>
	implements BaseHandler
{
	
	protected final C client;
	
	public AbstractBaseHandler(C client) {
		this.client = client;
	}
	
	protected boolean isLoopback() {
		return client.getRemoteAddress().toString().contains("127.0.0.1"); // TODO better implementation
	}
	
	protected void assertTrue(boolean b, String message, Object... obj) throws CriticalException {
		if (!b) throw new CriticalException(String.format(message, obj));
	}
	
	protected void assertFalse(boolean b, String message, Object... obj) throws CriticalException {
		if (b) throw new CriticalException(String.format(message, obj));
	}
	
	protected String getClearAddress() {
        String address = client.getRemoteAddress().toString();
        return address.substring(1, address.indexOf(':'));
	}

	protected Account findAccountOrPlayer(String name) {
		Account target = client.service().repositories().accounts().findByNickname(name);
		if (target == null) {
			Player player = client.service().repositories().players().find(name);
			if (player != null) {
				target = player.getOwner();
			}
		}
		return target;
	}
	
	public DofusLogger tchat(){
		return new TchatLogger(client, client.service().config());
	}
	
	public DofusLogger console(){
		return new ConsoleLogger(client, client.service().config());
	}
	
}
