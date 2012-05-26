package org.shivas.server.core.events.events;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.database.models.GameItem;

public class ItemCreationEvent implements Event {

	private GameItem item;
	
	public ItemCreationEvent(GameItem item) {
		this.item = item;
	}

	@Override
	public EventType type() {
		return EventType.ITEM_CREATION;
	}

	/**
	 * @return the item
	 */
	public GameItem item() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(GameItem item) {
		this.item = item;
	}

}
