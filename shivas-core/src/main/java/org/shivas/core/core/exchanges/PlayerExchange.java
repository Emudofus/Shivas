package org.shivas.core.core.exchanges;

import com.google.common.collect.Maps;
import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.events.EventDispatchers;
import org.shivas.core.core.interactions.AbstractInteraction;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.interactions.InteractionType;
import org.shivas.core.core.interactions.LinkedInteraction;
import org.shivas.core.core.items.ExchangeBag;
import org.shivas.core.core.items.PlayerBag;
import org.shivas.core.database.models.GameItem;
import org.shivas.core.services.game.GameClient;

import java.security.InvalidParameterException;
import java.util.Map;

public class PlayerExchange extends AbstractInteraction implements LinkedInteraction {
	
	private final GameClient source, target;
	private final Map<GameClient, ExchangeBag> bags = Maps.newIdentityHashMap();
	
	private final EventDispatcher event = EventDispatchers.create();

	public PlayerExchange(GameClient source, GameClient target) {
		this.source = source;
		this.target = target;
		
		bags.put(source, new ExchangeBag(source));
		bags.put(target, new ExchangeBag(target));
	}

    protected ExchangeBag bag(GameClient client) {
        ExchangeBag bag = bags.get(client);
        if (bag == null) throw new InvalidParameterException("this client has no exchange bag");
        return bag;
    }

    protected GameClient other(GameClient client) {
        if (client == source) return target;
        else if (client == target) return source;
        else throw new InvalidParameterException("client is neither source nor target");
    }

    protected ExchangeBag other(ExchangeBag bag) {
        return bags.get(other(bag.getOwner()));
    }

    protected void clearBags() {
        for (ExchangeBag bag : bags.values()) {
            PlayerBag playerBag = bag.getOwner().player().getBag();
            playerBag.plusKamas(bag.getKamas());
            bag.setKamas(0);

            for (GameItem item : bag) {
                GameItem original = bag.getOwner().player().getBag().get(item.getId());
                original.plusQuantity(item.getQuantity());
                item.setQuantity(0);
            }
        }
    }

    protected void checkReady() throws InteractionException {
        for (ExchangeBag bag : bags.values()) {
            if (bag.isReady()) {
                setReady(bag.getOwner());
            }
        }
    }

    protected void transferKamas(ExchangeBag first, ExchangeBag second) {
        second.getOwner().player().getBag().plusKamas(first.getKamas());
        first.getOwner().player().getBag().minusKamas(first.getKamas());
        first.setKamas(0);
    }

    protected void transferItems(ExchangeBag first, ExchangeBag second) {
        PlayerBag firstPlayerBag = first.getOwner().player().getBag(),
                  secondPlayerBag = second.getOwner().player().getBag();

        for (GameItem item : first) {
            GameItem original = first.getOwner().player().getBag().get(item.getId());
            if (original.getQuantity() == 0) {
                firstPlayerBag.delete(original);

                first.getOwner().write(ItemGameMessageFormatter.deleteMessage(original.getId()));
            } else {
                first.getOwner().write(ItemGameMessageFormatter.quantityMessage(original.getId(), original.getQuantity()));
            }

            GameItem same = secondPlayerBag.sameAs(item);
            if (same != null) {
                same.plusQuantity(item.getQuantity());
                item.setQuantity(0);

                second.getOwner().write(ItemGameMessageFormatter.quantityMessage(same.getId(), same.getQuantity()));
            } else {
                secondPlayerBag.persist(item);

                second.getOwner().write(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
            }
        }
    }

    protected void transfer() throws InteractionException {
        ExchangeBag first  = bags.get(source),
                    second = bags.get(target);

        transferKamas(first, second);
        transferKamas(second, first);

        source.write(source.player().getStats().packet());
        target.write(target.player().getStats().packet());

        transferItems(first, second);
        transferItems(second, first);

        source.write(ItemGameMessageFormatter.inventoryStatsMessage(source.player().getStats().pods()));
        target.write(ItemGameMessageFormatter.inventoryStatsMessage(target.player().getStats().pods()));
    }

    protected void writeToAll(String message) {
        source.write(message);
        target.write(message);
    }

	@Override
	public GameClient getSource() {
		return source;
	}

	@Override
	public GameClient getTarget() {
		return target;
	}

    public boolean isReady() {
        for (ExchangeBag bag : bags.values()) {
            if (!bag.isReady()) {
                return false;
            }
        }
        return true;
    }

	@Override
	public InteractionType getInteractionType() {
		return InteractionType.PLAYER_EXCHANGE;
	}

	@Override
	public void cancel() throws InteractionException {
        writeToAll(TradeGameMessageFormatter.tradeQuitMessage());

        clearBags();

        event.unsubscribe(source.eventListener());
        event.unsubscribe(target.eventListener());
	}

	@Override
	protected void internalEnd() throws InteractionException {
        transfer();

        writeToAll(TradeGameMessageFormatter.tradeSuccessMessage());
	}

	@Override
	public void begin() throws InteractionException {
		writeToAll(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.PLAYER));
		
		event.subscribe(source.eventListener());
		event.subscribe(target.eventListener());
	}

    public void setReady(GameClient owner) throws InteractionException {
        ExchangeBag bag = bag(owner);

        if (bag.isEmpty() && other(bag).isEmpty()) {
            owner.write(BasicGameMessageFormatter.noOperationMessage());
        } else {
            bag.setReady();

            if (isReady()) {
                end();
            } else {
                event.publish(new ReadyExchangeEvent(this, owner, bag.isReady()));
            }
        }
    }
	
	public void setKamas(GameClient owner, long kamas) throws InteractionException {
        checkReady();

		ExchangeBag bag = bags.get(owner);
		
		if (!bag.hasEnoughKamas()) {
			throw new InteractionException("you have not enough kamas to perform this request");
		}
		
		bag.setKamas(kamas);

        event.publish(new KamasExchangeEvent(this, owner, bag.getKamas()));
	}
	
	public void addItem(GameClient owner, GameItem item, int quantity) throws InteractionException {
        checkReady();

		if (quantity > item.getQuantity()) {
            throw new InteractionException("not enough quantity");
        }

        ExchangeBag bag = bag(owner);

        GameItem copy = bag.get(item.getId());
        if (copy == null) {
            copy = item.slice(quantity);
            bag.add(copy);
        } else {
            copy.plusQuantity(quantity);
            item.minusQuantity(quantity);
        }

        event.publish(new ItemExchangeEvent(ExchangeEventType.ADD_ITEM, this, owner, copy));
	}

    public void removeItem(GameClient owner, GameItem item, int quantity) throws InteractionException {
        checkReady();

        ExchangeBag bag = bag(owner);

        GameItem copy = bag.get(item.getId());
        if (copy == null) {
            throw new InteractionException("can't find copy item " + item.getId());
        }
        if (quantity > copy.getQuantity()) {
            throw new InteractionException("not enough quantity");
        }

        copy.minusQuantity(quantity);
        item.plusQuantity(quantity);

        event.publish(new ItemExchangeEvent(
                copy.getQuantity() == 0 ? ExchangeEventType.REMOVE_ITEM : ExchangeEventType.UPDATE_ITEM,
                this,
                owner,
                copy
        ));
    }

}
