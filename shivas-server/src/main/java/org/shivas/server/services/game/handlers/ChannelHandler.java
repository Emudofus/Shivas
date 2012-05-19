package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class ChannelHandler extends AbstractBaseHandler<GameClient> {

	public ChannelHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	@Override
	public void init() throws Exception {
		client.service().channels().subscribeAll(client.account().getChannels(), client.eventListener());
	}

	@Override
	public void handle(String message) throws Exception {
		switch (message.charAt(1)) {
		case 'C':
			if (message.charAt(2) == '+') {
				parseAddChannelMessage(ChannelEnum.valueOf(message.charAt(3)));
			} else {
				parseRemoveChannelMessage(ChannelEnum.valueOf(message.charAt(3)));
			}
			break;
		}
	}

	@Override
	public void onClosed() {
		client.service().channels().unsubscribeAll(client.account().getChannels(), client.eventListener());
	}

	private void parseAddChannelMessage(ChannelEnum channel) {
		if (!client.account().getChannels().contains(channel)) {
			client.account().getChannels().add(channel);
			client.service().repositories().accounts().saveLater(client.account());
		}
		session.write(ChannelGameMessageFormatter.addChannelMessage(channel));
	}

	private void parseRemoveChannelMessage(ChannelEnum channel) {
		if (client.account().getChannels().contains(channel)) {
			client.account().getChannels().remove(channel);
			client.service().repositories().accounts().saveLater(client.account());
		}
		session.write(ChannelGameMessageFormatter.removeChannelMessage(channel));
	}

}
