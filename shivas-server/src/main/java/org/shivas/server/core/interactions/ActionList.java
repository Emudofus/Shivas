package org.shivas.server.core.interactions;

import java.util.List;

import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.NewInteractionEvent;
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
		
		event.publish(new NewInteractionEvent(client, action));
		
		return action;
	}
	
	public <T extends Action> T add(final T action) {
		actions.add(0, action);
		
		event.publish(new NewInteractionEvent(client, action));
		
		return action;
	}
	
	public <T extends Invitation> T push(final T invitation) {
		if (invitation.getSource() != client) {
			throw new IllegalArgumentException("ActionList's owner isn't the source");
		}
		
		actions.add(invitation);
		invitation.getTarget().actions().actions.add(invitation);

		event.publish(new NewInteractionEvent(client, invitation));
		invitation.getTarget().actions().event.publish(new NewInteractionEvent(invitation.getTarget(), invitation));
		
		return invitation;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Action> T current() {
		return (T) actions.get(actions.size() - 1);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Action> T remove() {
		T action = (T) actions.remove(actions.size() - 1);
		if (action.getClass().isAssignableFrom(Invitation.class)) {
			Invitation invitation = (Invitation) action;
			invitation.getTarget().actions().actions.remove(action);
		}
		
		return action;
	}
	
}
