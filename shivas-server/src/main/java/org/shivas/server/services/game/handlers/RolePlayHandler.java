package org.shivas.server.services.game.handlers;


import org.joda.time.DateTime;
import org.shivas.protocol.client.formatters.*;
import org.shivas.server.services.AbstractBaseHandlerContainer;
import org.shivas.server.services.game.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RolePlayHandler extends AbstractBaseHandlerContainer<GameClient> {
	
	private static final Logger log = LoggerFactory.getLogger(RolePlayHandler.class);

	public RolePlayHandler(GameClient client) {
		super(client);
	}

	@Override
	public void init() throws Exception {
		super.init();
		
		client.write(ChannelGameMessageFormatter.addChannelsMessage(client.account().getChannels()));
		client.write(SpellGameMessageFormatter.spellListMessage(client.player().getSpells().toBaseSpellType()));
		client.write(ChannelGameMessageFormatter.enabledEmotesMessage("")); // TODO emotes
		client.write(ItemGameMessageFormatter.inventoryStatsMessage(client.player().getStats().pods()));
		client.write(FriendGameMessageFormatter.notifyFriendOnConnectMessage(client.account().getContacts().isNotificationListener()));
		client.write(InfoGameMessageFormatter.welcomeMessage());
		if (!client.account().firstConnection()) {
			client.write(InfoGameMessageFormatter.lastConnectionInformationMessage(
					client.account().getLastConnection(),
					client.account().getLastAddress()
			));
		}
		client.write(InfoGameMessageFormatter.currentAddressInformationMessage(getClearAddress()));

		client.account().setLastConnection(DateTime.now());
		client.account().setLastAddress(getClearAddress());
		client.account().incrementNbConnections();
		client.service().repositories().accounts().saveLater(client.account());
		
		super.initChildren();
	}

	@Override
	public void onClosed() {
		super.onClosed();
		
		client.account().setConnected(false);
		client.account().setCurrentPlayer(null);
		client.player().setClient(null);
		client.service().repositories().accounts().save(client.account());
	}

	@Override
	protected void configure() {
		add('A', new ApproachHandler(client));
		add('B', new BasicHandler(client));
		add('c', new ChannelHandler(client));
        add('D', new DialogHandler(client));
		add('E', new ExchangeHandler(client));
		add('F', new FriendHandler(client));
		add('G', new GameHandler(client));
        add('g', new GuildHandler(client));
		add('i', new EnnemyHandler(client));
		add('O', new ItemHandler(client));
		add('P', new PartyHandler(client));
		add('S', new SpellHandler(client));
		add('W', new WaypointHandler(client));
	}

	@Override
	protected void onReceivedUnknownMessage(String message) {
		log.debug("unknown message {}", message);
		
		client.write(BasicGameMessageFormatter.noOperationMessage());
	}

}
