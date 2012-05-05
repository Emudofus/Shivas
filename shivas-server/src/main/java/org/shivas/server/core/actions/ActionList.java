package org.shivas.server.core.actions;

import java.util.List;

import org.shivas.common.observable.AbstractObservable;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.game.GameClient;

import com.google.common.collect.Lists;

public class ActionList extends AbstractObservable<ActionList.Listener, Action> {

	public static interface Listener {
		void listenAction(Player player, Action action);
	}
	
	private GameClient client;
	private List<Action> actions = Lists.newArrayList();

	public ActionList(GameClient client) {
		this.client = client;
	}

	protected void notifyObserver(Listener observer, Action arg) {
		observer.listenAction(client.player(), arg);
	}
	
	public <T extends Action> T push(final T action) {
		actions.add(action);

		client.service().actionWorker().execute(new Runnable() {
			public void run() {
				notifyObservers(action);
			}
		});
		
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
