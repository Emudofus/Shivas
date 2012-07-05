package org.shivas.server.services.game.handlers;

import java.util.Collection;
import java.util.Map;

import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemSet;
import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;
import org.shivas.server.utils.Converters;

import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Multimap;

public class ItemHandler extends AbstractBaseHandler<GameClient> {

	public ItemHandler(GameClient client) {
		super(client);
	}
	
	private Map<ItemSet, Collection<GameItem>> getItemSets() {
		Multimap<ItemSet, GameItem> sets = ArrayListMultimap.create();
		
		for (GameItem item : client.player().getBag().equiped()) {
			if (item.getTemplate().getItemSet() == null) continue;
			
			sets.put(item.getTemplate().getItemSet(), item);
		}
		
		return sets.asMap();
	}

	@Override
	public void init() throws Exception {
		for (Map.Entry<ItemSet, Collection<GameItem>> entry : getItemSets().entrySet()) {
			if (entry.getValue().size() < 2) continue;
			
			Collection<GameItem> items = entry.getValue();
			ItemSet set = entry.getKey();
			Collection<ItemEffect> effects = set.getEffects(items.size());
			
			client.write(ItemGameMessageFormatter.addItemSetMessage(
					set.getId(),
					Collections2.transform(items, Converters.GAMEITEM_TO_BASEITEMTYPE),
					Collections2.transform(effects, Converters.ITEMEFFECT_TO_BASEITEMEFFECTTYPE)
			));
		}
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
		assertFalse(item.getPosition().equipment(), "you can't delete an equiped item");
		
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
		
		applyItemSet(item);
		
		client.write(client.player().getStats().refresh().packet());
		client.write(ItemGameMessageFormatter.inventoryStatsMessage(client.player().getStats().pods()));
		
		client.player().getLocation().getMap().updateAccessories(client.player());
	}
	
	private void applyItemSet(final GameItem item) {
		final ItemSet set = item.getTemplate().getItemSet();
		
		Collection<GameItem> items = Collections2.filter(client.player().getBag().equiped(), new Predicate<GameItem>() {
			public boolean apply(GameItem input) {
				return input == null ? false :
					input.getTemplate().getItemSet() == set;
			}
		});
		
		int count = items.size(); // keep size in memory => avoids multiple size computing
		
		if (count >= 2) {
			Collection<ItemEffect> effects = set.getEffects(count);
			
			client.write(ItemGameMessageFormatter.addItemSetMessage(
					set.getId(),
					Collections2.transform(items, Converters.GAMEITEM_TO_BASEITEMTYPE),
					Collections2.transform(effects, Converters.ITEMEFFECT_TO_BASEITEMEFFECTTYPE)
			));
		} else {
			client.write(ItemGameMessageFormatter.removeItemSetMessage(set.getId()));
		}
	}

}
