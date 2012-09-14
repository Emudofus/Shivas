import org.shivas.server.core.plugins.Plugin
import org.shivas.server.core.plugins.Startable
import org.shivas.server.core.commands.Command
import org.shivas.server.core.logging.DofusLogger
import org.shivas.server.services.game.GameClient
import org.shivas.common.params.Types
import org.shivas.common.params.Conditions
import org.shivas.common.params.Parameters
import javax.swing.Timer
import java.awt.event.ActionListener

class Shivas { static def instance }
Shivas.instance = hook

class PubManagerPlugin extends Plugin {
	final PubManagerService pubManager = new PubManagerService()

	final Collection<Startable> services = [
		pubManager
	]

	final Collection<Command> commands = [
		new SetMessageCommand(pubManager),
		new SendPubCommand(pubManager)
	]
}

class PubManagerService implements Startable {
	static final int RATE = 20

	private def timer

	String message = "default message"

	public PubManagerService() {
		timer = new Timer(
			RATE * 1000,
			{ send() } as ActionListener
		)
	}

	void send() {
		Shivas.instance.game.channels().system().send(null, message)
	}

	void start() {
		timer.start()
	}

	void stop() {
		timer.stop()
	}
}

class SetMessageCommand implements Command {
	private def pubManager
	public SetMessageCommand(pubManager) {
		this.pubManager = pubManager
	}

	String name() { return "pub" }
	String help() { return "Set the pub message" }

	Conditions conditions() {
		Conditions conds = new Conditions()
		conds.add("msg", Types.STRING, "The message that PubManagerService will sent")

		return conds
	}

	boolean canUse(GameClient client) {
		return client.account().hasRights()
	}

	void use(GameClient client, DofusLogger log, Parameters params) {
		String msg = params.get("msg", String).replace("&gt;", ">").replace("&lt;", "<")

		pubManager.message = msg

		log.info("The message has been setted.")
	}
}

class SendPubCommand implements Command {
	private def pubManager
	public SendPubCommand(pubManager) {
		this.pubManager = pubManager
	}

	String name() { return "send_pub" }
	Conditions conditions() { return Conditions.EMPTY }
	String help() { return "Send the pub message" }

	boolean canUse(GameClient client) {
		return client.account().hasRights()
	}

	void use(GameClient client, DofusLogger log, Parameters params) {
		pubManager.send()
	}
}

plugin = new PubManagerPlugin()
