package org.shivas.server.core.interactions;

import java.util.List;

import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.NewInteractionEvent;
import org.shivas.server.services.game.GameClient;

import com.google.common.collect.Lists;

public class InteractionList {
	
	private GameClient client;
	private List<Interaction> interactions = Lists.newArrayList();
	
	protected final EventDispatcher event = EventDispatchers.create();

	public InteractionList(GameClient client) {
		this.client = client;
	}

	public void subscribe(EventListener listener) {
		event.subscribe(listener);
	}

	public void unsubscribe(EventListener listener) {
		event.unsubscribe(listener);
	}
	
	public int size() {
		return interactions.size();
	}
	
	public <T extends Interaction> T push(final T action) {
		interactions.add(action);
		
		event.publish(new NewInteractionEvent(client, action));
		
		return action;
	}
	
	public <T extends Interaction> T add(final T action) {
		interactions.add(0, action);
		
		event.publish(new NewInteractionEvent(client, action));
		
		return action;
	}
	
	public <T extends Invitation> T push(final T invitation) {
		if (invitation.getSource() != client) {
			throw new IllegalArgumentException("InteractionList's owner isn't the source");
		}
		
		interactions.add(invitation);
		invitation.getTarget().interactions().interactions.add(invitation);

		event.publish(new NewInteractionEvent(client, invitation));
		invitation.getTarget().interactions().event.publish(new NewInteractionEvent(invitation.getTarget(), invitation));
		
		return invitation;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Interaction> T current() {
		return (T) interactions.get(interactions.size() - 1);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Interaction> T remove() {
		T action = (T) interactions.remove(interactions.size() - 1);
		if (action.getClass().isAssignableFrom(Invitation.class)) {
			Invitation invitation = (Invitation) action;
			invitation.getTarget().interactions().interactions.remove(action);
		}
		
		return action;
	}
	
}
