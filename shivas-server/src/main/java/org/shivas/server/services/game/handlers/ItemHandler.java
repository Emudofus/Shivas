package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;

public class ItemHandler extends AbstractBaseHandler<GameClient> {

	public ItemHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public void onClosed() {
	}

	@Override
	public void handle(String message) throws Exception {
		String[] args;
		switch (message.charAt(1)) {
		case 'd':
			args = message.substring(2).split("\\|");
			parseDeleteMessage(
					client.player().getBag().get(Long.parseLong(args[0])),
					Integer.parseInt(args[1])
			);
			break;
		}
	}

	private void parseDeleteMessage(GameItem item, int quantity) throws CriticalException {
		if (quantity > item.getQuantity()) {
			throw new CriticalException("not enough quantity");
		} else if (quantity < item.getQuantity()) {
			item.minusQuantity(quantity);
			
			session.write(ItemGameMessageFormatter.quantityMessage(item.id(), item.getQuantity()));
		} else {
			client.player().getBag().delete(item);
			
			session.write(ItemGameMessageFormatter.deleteMessage(item.id()));
		}
		
		session.write(client.player().getStats().packet());
	}

}
