package org.shivas.server.core.exchanges;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Map;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.Acceptable;
import org.shivas.server.core.interactions.Declinable;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.core.interactions.LinkedInteraction;
import org.shivas.server.core.items.ExchangeBag;
import org.shivas.server.core.items.PlayerBag;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.services.game.GameClient;

import com.google.common.collect.Maps;

public class PlayerExchange extends AbstractInteraction implements LinkedInteraction, Acceptable, Declinable {
	
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

    protected void transfer() throws InteractionException {
        for (ExchangeBag bag : bags.values()) {
            GameClient client = bag.getOwner(),
                       other  = other(client);
            PlayerBag source = client.player().getBag(),
                      target  = other.player().getBag();

            source.minusKamas(bag.getKamas());
            target.plusKamas(bag.getKamas());
            bag.setKamas(0);

            other.write(other.player().getStats().packet());

            for (GameItem item : bag) {
                GameItem original = bag.getOwner().player().getBag().get(item.getId());
                if (original.getQuantity() == 0) {
                    client.write(ItemGameMessageFormatter.deleteMessage(original.getId()));

                    source.delete(original);
                } else {
                    client.write(ItemGameMessageFormatter.quantityMessage(original.getId(), original.getQuantity()));
                }

                GameItem same = target.sameAs(item);
                if (same != null) {
                    same.plusQuantity(item.getQuantity());

                    other.write(ItemGameMessageFormatter.quantityMessage(same.getId(), same.getQuantity()));
                } else {
                    target.persist(item);

                    other.write(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
                }
                item.setQuantity(0);
            }

            other.write(ItemGameMessageFormatter.inventoryStatsMessage(other.player().getStats().pods()));
        }
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
		decline();
	}

	@Override
	protected void internalEnd() throws InteractionException {
        transfer();

        writeToAll(TradeGameMessageFormatter.tradeSuccessMessage());
	}

	protected void writeToAll(String message) {
		source.write(message);
		target.write(message);
	}

	@Override
	public void begin() throws InteractionException {
		writeToAll(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.PLAYER));
		
		event.subscribe(source.eventListener());
		event.subscribe(target.eventListener());
	}

	@Override
	public void decline() throws InteractionException {
		writeToAll(TradeGameMessageFormatter.tradeQuitMessage());

        clearBags();
		
		event.unsubscribe(source.eventListener());
		event.unsubscribe(target.eventListener());
	}

	@Override
	public void accept() throws InteractionException {
        end();
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
