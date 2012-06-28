package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;

public class ItemHandler extends AbstractBaseHandler<GameClient> {

	public ItemHandler(GameClient client) {
		super(client);
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
			
		case 'M':
			args = message.substring(2).split("\\|");
			parseMoveMessage(
					client.player().getBag().get(Long.parseLong(args[0])),
					ItemPositionEnum.valueOf(Integer.parseInt(args[1]))
			);
			break;
		}
	}

	private void parseDeleteMessage(GameItem item, int quantity) throws CriticalException {
		assertFalse(item == null, "unknown item");
		assertTrue(quantity <= item.getQuantity(), "not enough");
		
		if (quantity < item.getQuantity()) {
			item.minusQuantity(quantity);
			
			client.write(ItemGameMessageFormatter.quantityMessage(item.id(), item.getQuantity()));
		} else {
			client.player().getBag().delete(item);
			
			client.write(ItemGameMessageFormatter.deleteMessage(item.id()));
		}
		
		client.write(client.player().getStats().packet());
	}

	private void parseMoveMessage(GameItem item, ItemPositionEnum position) throws CriticalException {
		assertFalse(item == null, "unknown item");
		assertTrue(item.getQuantity() >= 1, "not enough");
		assertFalse(item.getPosition() == position, "this item is already on %s", position);
		assertTrue(client.player().getBag().validMovement(item, position), "invalid movement");

		GameItem same;
		if (position == ItemPositionEnum.NotEquiped && (same = client.player().getBag().sameAs(item)) != null) {
				same.plusQuantity(1);
				item.setQuantity(0);
				
				client.service().repositories().items().saveLater(same);
				client.player().getBag().delete(item);
				
				client.write(ItemGameMessageFormatter.deleteMessage(item.id()));
				client.write(ItemGameMessageFormatter.quantityMessage(same.id(), same.getQuantity()));
		} else {
			if (item.getQuantity() > 1) {
				GameItem sliced = item.sliceOne();
				
				client.player().getBag().persist(sliced);
				client.service().repositories().items().saveLater(item);
				
				client.write(ItemGameMessageFormatter.addItemMessage(sliced.toBaseItemType()));
				client.write(ItemGameMessageFormatter.quantityMessage(item.id(), item.getQuantity()));
				
				item = sliced;
			}
			
			item.setPosition(position);
			client.service().repositories().items().saveLater(item);
			
			client.write(ItemGameMessageFormatter.itemMovementMessage(item.id(), position));
		}
		
		client.write(client.player().getStats().refresh().packet());
		client.player().getLocation().getMap().updateAccessories(client.player());
	}

}
