package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.joda.time.DateTime;
import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.server.core.channels.Channel;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
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
					if (msg.startsWith(client.service().config().cmdPrefix())) {
						parseClientCommandMessage(message.substring(client.service().config().cmdPrefix().length()));
					} else {
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
		session.write(BasicGameMessageFormatter.currentDateMessage(DateTime.now()));
	}

	private void parseSendClientMultiMessage(ChannelEnum chan, String message) {
		if (client.account().getChannels().contains(chan)) {
			Channel channel = client.service().channels().get(chan);
			channel.send(client.player(), message);
		} else {
			session.write(BasicGameMessageFormatter.noOperationMessage());
		}
	}

	private void parseSendPrivateMessage(Player target, String message) {
		target.sendMessage(client.player(), message);
	}

	private void parseAdminCommandMessage(String command) throws CriticalException {
		if (!client.account().hasRights()) {
			throw new CriticalException("this client hasn't enough rights");
		}
		
		client.service().cmdEngine().use(client, console(), command);
	}

	private void parseClientCommandMessage(String command) {
		client.service().cmdEngine().use(client, tchat(), command);
	}

}
