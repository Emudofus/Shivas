package org.shivas.server.services.game.handlers;

import java.util.Arrays;

import org.apache.mina.core.session.IoSession;
import org.joda.time.DateTime;
import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.server.core.channels.Channel;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class BasicHandler extends AbstractBaseHandler<GameClient> {

	public BasicHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	public void init() throws Exception {
	}

	public void handle(String message) throws Exception {
		String[] args;
		switch (message.charAt(1)) {
		case 'A':
			parseAdminCommandMessage(message.substring(2));
			break;
		
		case 'D':
			parseCurrentDateMessage();
			break;
			
		case 'M':
			if (client.account().isMuted()) {
				// TODO logging
			} else {
				args = message.substring(2).split("\\|");
				if (args[0].length() > 1){
					parseSendPrivateMessage(
							client.service().repositories().players().find(args[0]),
							args[1]
					);
				} else {
					parseSendClientMultiMessage(
							ChannelEnum.valueOf(message.charAt(2)),
							args[1]
					);
				}
			}
			break;
		}
	}

	public void onClosed() {
	}

	private void parseCurrentDateMessage() {
		session.write(BasicGameMessageFormatter.currentDateMessage(DateTime.now()));
	}

	private void parseSendClientMultiMessage(ChannelEnum chan, String message) {
		Channel channel = client.service().channels().get(chan);
		
		channel.send(client.player(), message);
	}

	private void parseSendPrivateMessage(Player target, String message) {
		target.sendMessage(client.player(), message);
	}

	private void parseAdminCommandMessage(String command) {
		String[] args = command.split(" ");
		
		String name = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		
		// TODO admin commands
	}

}
