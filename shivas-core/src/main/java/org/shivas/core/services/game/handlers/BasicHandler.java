package org.shivas.core.services.game.handlers;

import org.joda.time.DateTime;
import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.core.core.channels.Channel;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.AbstractBaseHandler;
import org.shivas.core.services.CriticalException;
import org.shivas.core.services.game.GameClient;

public class BasicHandler extends AbstractBaseHandler<GameClient> {

	public BasicHandler(GameClient client) {
		super(client);
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
				tchat().error("Vous ne pouvez pas parler car un modérateur vous a retiré la parole.");
			} else {
				args = message.substring(2).split("\\|");
				String msg = args[1];
				if (args[0].length() > 1){
					parseSendPrivateMessage(
							client.service().repositories().players().find(args[0]),
							msg
					);
				} else {
					if (client.service().config().cmdEnabled() &&
					    msg.startsWith(client.service().config().cmdPrefix()))
					{
						parseClientCommandMessage(msg.substring(client.service().config().cmdPrefix().length()));
					}
					else {
						parseSendClientMultiMessage(
								ChannelEnum.valueOf(message.charAt(2)),
								msg
						);
					}
				}
			}
			break;
		}
	}

	public void onClosed() {
	}

	private void parseCurrentDateMessage() {
		client.write(BasicGameMessageFormatter.currentDateMessage(DateTime.now()));
	}

	private void parseSendClientMultiMessage(ChannelEnum chan, String message) {
		if (client.account().getChannels().contains(chan)) {
			Channel channel = client.service().channels().get(chan);
			channel.send(client.player(), message);
		} else {
			client.write(BasicGameMessageFormatter.noOperationMessage());
		}
	}

	private void parseSendPrivateMessage(Player target, String message) {
		if (target.canReceiveMessageFrom(client.player())) {
			target.sendMessage(client.player(), message);
		} else {
			client.write(BasicGameMessageFormatter.noOperationMessage());
		}
	}

	private void parseAdminCommandMessage(String command) throws CriticalException {
		assertTrue(client.account().hasRights(), "the client hasn't enough rights");
		
		client.service().cmdEngine().use(client, console(), command);
	}

	private void parseClientCommandMessage(String command) {
		client.service().cmdEngine().use(client, tchat(), command);
	}

}
