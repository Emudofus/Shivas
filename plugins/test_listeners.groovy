import org.shivas.server.services.ServiceListener
import org.shivas.server.services.game.GameClient
import org.shivas.server.core.events.EventListener
import org.shivas.server.core.events.Event
import org.shivas.server.core.events.EventType
import org.shivas.server.core.plugins.Plugin

import org.slf4j.LoggerFactory

class Shivas { static def instance }
Shivas.instance = hook

class TestListenersPlugin extends Plugin {
	Collection<ServiceListener<GameClient>> gameListeners = [
		new FirstGameServiceListener()
	]
}

class FirstGameServiceListener implements ServiceListener<GameClient> {
	void connected(GameClient client) {
		FirstEventListener listener = new FirstEventListener(client)
		client.eventListener().add(listener)
	}

	void disconnected(GameClient client) {
	}
}

class FirstEventListener implements EventListener {
	static final def log = LoggerFactory.getLogger(FirstEventListener)

	private GameClient client
	public FirstEventListener(GameClient client) {
		this.client = client
	}

	void listen(Event event) {
		if (event.type() == EventType.CHANGE_MAP && event.player == client.player()) {
			listenChangeMap()
		}
	}

	private void listenChangeMap() {
		System.out.format("%s has changed of map\n", client.player().name)
	}
}

plugin = new TestListenersPlugin()
