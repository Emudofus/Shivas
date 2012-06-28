package org.shivas.server.core.actions;

import java.util.List;

import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.NewActionEvent;
import org.shivas.server.services.game.GameClient;

import com.google.common.collect.Lists;

public class ActionList {
	
	private GameClient client;
	private List<Action> actions = Lists.newArrayList();
	
	protected final EventDispatcher event = EventDispatchers.create();

	public ActionList(GameClient client) {
		this.client = client;
	}

	public void subscribe(EventListener listener) {
		event.subscribe(listener);
	}

	public void unsubscribe(EventListener listener) {
		event.unsubscribe(listener);
	}
	
	public <T extends Action> T push(final T action) {
		actions.add(action);
		
		event.publish(new NewActionEvent(client, action));
		
		return action;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Action> T current() {
		return (T) actions.get(actions.size() - 1);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Action> T remove() {
		return (T) actions.remove(actions.size() - 1);
	}
	
}
