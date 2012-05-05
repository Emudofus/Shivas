package org.shivas.server.services.game.handlers;

import java.util.ArrayList;

import org.apache.mina.core.session.IoSession;
import org.joda.time.DateTime;
import org.shivas.common.maths.BasicLimitedValue;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.protocol.client.formatters.FriendGameMessageFormatter;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.protocol.client.formatters.SpellGameMessageFormatter;
import org.shivas.protocol.client.types.BaseSpellType;
import org.shivas.server.services.AbstractBaseHandlerContainer;
import org.shivas.server.services.game.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RolePlayHandler extends AbstractBaseHandlerContainer<GameClient> {
	
	private static final Logger log = LoggerFactory.getLogger(RolePlayHandler.class);

	public RolePlayHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	@Override
	public void init() throws Exception {
		super.init();
		
		session.write(ChannelGameMessageFormatter.addChannelsMessage(client.account().getChannels()));
		session.write(SpellGameMessageFormatter.spellListMessage(new ArrayList<BaseSpellType>(0))); // TODO spells
		session.write(ChannelGameMessageFormatter.enabledEmotesMessage("")); // TODO emotes
		session.write(ItemGameMessageFormatter.inventoryStatsMessage(new BasicLimitedValue(1000))); // TODO statistics
		session.write(FriendGameMessageFormatter.notifyFriendOnConnectMessage(false)); // TODO friends
		session.write(InfoGameMessageFormatter.welcomeMessage());
		if (!client.account().firstConnection()) {
			session.write(InfoGameMessageFormatter.lastConnectionInformationMessage(
					client.account().getLastConnection(),
					client.account().getLastAddress()
			));
		}
		session.write(InfoGameMessageFormatter.currentAddressInformationMessage(getClearAddress()));

		client.account().setLastConnection(DateTime.now());
		client.account().setLastAddress(getClearAddress());
		client.account().incrementNbConnections();
		client.service().repositories().accounts().save(client.account());
	}

	@Override
	public void onClosed() {
		super.onClosed();
		
		client.account().setConnected(false);
		client.service().repositories().accounts().save(client.account());
	}

	@Override
	protected void configure() {
		add('A', new ApproachHandler(client, session));
		add('B', new BasicHandler(client, session));
		add('C', new ChannelHandler(client, session));
		add('G', new GameHandler(client, session));
	}

	@Override
	protected void onReceivedUnknownMessage(String message) {
		log.debug("unknown message {}", message);
		
		session.write(BasicGameMessageFormatter.noOperationMessage());
	}

}
