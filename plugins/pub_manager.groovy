import org.shivas.server.core.plugins.Plugin
import org.shivas.server.core.plugins.Startable
import org.shivas.server.core.commands.Command
import org.shivas.common.params.Types
import com.google.common.collect.Lists
import javax.swing.Timer

class PubManagerPlugin extends Plugin {
	final String author = "Blackrush"
	final String version = "alpha1"
	final String help = "Sends pub message in global channel"

	Collection<Startable> getServices() {
		return Lists.newArrayList(PubManagerService.INSTANCE)
	}

	Collection<Command> getCommands() {
		return Lists.newArrayList(new PubManagerCommand())
	}
}

class PubManagerService implements Startable {
	static final int RATE = 2 * 60
	static final PubManagerService INSTANCE = new PubManagerService()

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

class PubManagerCommand implements Command {
	String name() {
		return "pub"
	}

	Conditions conditions() {
		Conditions conds = new Conditions()
		conds.add("msg", Types.STRING, "The message that PubManagerService will sent")

		return conds
	}

	String help() {
		return "Set the pub message"
	}
	
	boolean canUse(GameClient client) {
		return client.account().hasRights()
	}

	void use(GameClient client, DofusLogger log, Parameters params) {
		String msg = params.get("msg", String)

		PubManagerService.INSTANCE.pubMessage = msg

		log.info("the message has been setted")
	}
}

plugin = new PubManagerPlugin()
