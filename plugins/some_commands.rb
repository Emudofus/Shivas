import org.shivas.server.core.plugins.Plugin
import org.shivas.server.core.commands.Command
import org.shivas.common.params.Conditions
import java.util.ArrayList

class SomeCommandsPlugin < Plugin
	def author; "Blackrush"; end
	def version; "alpha1"; end
	def help; "Some commands"; end

	def getCommands
		ArrayList.new [
			HelloWorldCommand.new
		]
	end
end

class HelloWorldCommand
	include Command

	def name; "hello"; end
	def conditions; Conditions.new; end
	def help; "Hello world"; end

	def canUse(client); true; end

	def use(client, log, params)
		log.info "hello world"
		log.info Shivas.config.dataPath()
	end
end

SomeCommandsPlugin.new
