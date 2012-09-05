import org.shivas.server.core.plugins.Plugin
import org.shivas.server.core.plugins.Startable
import com.google.common.collect.Lists
import javax.swing.Timer

class PubManagerPlugin extends Plugin {
	String getAuthor() {
		return "Blackrush"
	}

	String getVersion() {
		return "alpha1"
	}

	String getHelp() {
		return "Sends pub message in global channel"
	}

	Collection<Startable> getServices() {
		return Lists.newArrayList(new PubManagerService())
	}
}

class PubManagerService implements Startable {
	static final int RATE = 2 * 60

	private Timer timer

	String pubMessage

	public PubManagerService() {
		timer = new Timer(
			RATE * 1000,
			evt -> send()
		)
	}

	private void send() {
		Shivas.getGservice().system().send(null, pubMessage)
	}

	void start() {
		timer.start()
	}

	void stop() {
		timer.stop()
	}
}

plugin = new PubManagerPlugin()
