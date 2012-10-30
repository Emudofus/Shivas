import org.shivas.core.core.plugins.Plugin
import org.shivas.core.core.plugins.Startable
import org.shivas.core.core.commands.Command
import org.shivas.core.core.logging.DofusLogger
import org.shivas.core.services.game.GameClient
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
		new PubControllerCommand(pubManager),
		new SendPubCommand(pubManager)
	]
}

class PubManagerService implements Startable {
	static final int RATE = 20

	private def timer

	String message = "default message"
    boolean started

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
        if (started) return

		timer.start()
        started = true
	}

	void stop() {
        if (!started) return

		timer.stop()
        started = false
	}
}

class PubControllerCommand implements Command {
	private def pubManager
	public PubControllerCommand(pubManager) {
		this.pubManager = pubManager
	}

    String name = "pub"
    String help = "Control the pub manager"
    Conditions conditions = new Conditions() {{
        add("msg", Types.STRING, "The message that PubManagerService will sent", true)
        add("enable", Types.BOOLEAN, "Enable the PubManagerService or not", true)
    }}

	boolean canUse(GameClient client) {
		return client.account().hasRights()
	}

	void use(GameClient client, DofusLogger log, Parameters params) {
        def msg = params.get("msg", String)
        if (msg != null && msg.length() > 0) {
            pubManager.message = msg.replace("&gt;", ">").replace("&lt;", "<")

            log.info("The message has been setted.")
        }

        if (params.has("enable", Boolean)) {
            if (params.get("enable", Boolean)) {
                if (pubManager.started) {
                    log.error("PubManagerService is already running")
                } else {
                    pubManager.start()
                }
            } else {
                if (!pubManager.started) {
                    log.error("PubManagerService is already stopped")
                } else {
                    pubManager.stop()
                }
            }
        }
	}
}

class SendPubCommand implements Command {
	private def pubManager
	public SendPubCommand(pubManager) {
		this.pubManager = pubManager
	}

    String name = "send_pub"
    Conditions conditions = Conditions.EMPTY
    String help = "Send the pub message"

	boolean canUse(GameClient client) {
		return client.account().hasRights()
	}

	void use(GameClient client, DofusLogger log, Parameters params) {
		pubManager.send()
	}
}

plugin = new PubManagerPlugin()
