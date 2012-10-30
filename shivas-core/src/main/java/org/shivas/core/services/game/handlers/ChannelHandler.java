package org.shivas.core.services.game.handlers;

import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.core.core.channels.Channel;
import org.shivas.core.services.AbstractBaseHandler;
import org.shivas.core.services.game.GameClient;

public class ChannelHandler extends AbstractBaseHandler<GameClient> {

	public ChannelHandler(GameClient client) {
		super(client);
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

	private void parseAddChannelMessage(ChannelEnum chan) {
		if (!client.account().getChannels().contains(chan)) {
			client.account().getChannels().add(chan);
			client.service().repositories().accounts().saveLater(client.account());
			
			Channel channel = client.service().channels().get(chan);
			if (channel != null) {
				channel.subscribe(client.eventListener());
			}
		}
		client.write(ChannelGameMessageFormatter.addChannelMessage(chan));
	}

	private void parseRemoveChannelMessage(ChannelEnum chan) {
		if (client.account().getChannels().contains(chan)) {
			client.account().getChannels().remove(chan);
			client.service().repositories().accounts().saveLater(client.account());
			
			Channel channel = client.service().channels().get(chan);
			if (channel != null) {
				channel.unsubscribe(client.eventListener());
			}
		}
		client.write(ChannelGameMessageFormatter.removeChannelMessage(chan));
	}

}
