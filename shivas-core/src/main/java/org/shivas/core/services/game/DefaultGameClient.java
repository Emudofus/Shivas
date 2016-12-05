package org.shivas.core.services.game;

import org.apache.mina.core.session.IoSession;
import org.shivas.core.core.events.EventListenerContainer;
import org.shivas.core.core.interactions.InteractionList;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.core.parties.Party;
import org.shivas.core.database.models.Account;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.BaseHandler;
import org.shivas.core.services.IoSessionDecorator;
import org.shivas.protocol.client.formatters.GameMessageFormatter;

public final class DefaultGameClient extends IoSessionDecorator implements GameClient {
	
	private final GameService service;
	
	private Account account;
	private Player player;
	private Party party;
	private BaseHandler handler;
	private EventListenerContainer eventListener = new EventListenerContainer();
	private InteractionList interactions = new InteractionList(this);

	public DefaultGameClient(IoSession session, GameService service) {
		super(session);
		this.service = service;
	}

	public GameService service() {
		return service;
	}

	public void kick() {
		closeOnFlush();
	}

	public void kick(String message) {
		write(GameMessageFormatter.kickMessage("un administrateur", message))
			.addListener(arg0 -> closeOnFlush());
	}
	
	public BaseHandler handler() {
		return handler;
	}

	public void newHandler(BaseHandler handler) throws Exception {
		this.handler = handler;
		this.handler.init();
	}

	public Account account() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Player player() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public boolean hasParty() {
		return party != null;
	}

	@Override
	public Party party() {
		return party;
	}

	@Override
	public void setParty(Party party) {
		this.party = party;
	}

	public EventListenerContainer eventListener() {
		return eventListener;
	}

	public InteractionList interactions() {
		return interactions;
	}

	public boolean isBusy() {
		return interactions.size() > 0;
	}

	public DofusLogger tchat() {
		return handler.tchat();
	}

}
